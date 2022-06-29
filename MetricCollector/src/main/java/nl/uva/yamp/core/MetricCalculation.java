package nl.uva.yamp.core;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.CallGraphCollector;
import nl.uva.yamp.core.collector.CoverageCollector;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.collector.TargetCollector;
import nl.uva.yamp.core.enricher.DisjointMutantEnricher;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.MetricSet;
import nl.uva.yamp.core.model.TargetDirectory;
import nl.uva.yamp.core.writer.Writer;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MetricCalculation {

    private final ForkJoinPool forkJoinPool;
    private final TargetCollector targetCollector;
    private final CoverageCollector coverageCollector;
    private final CallGraphCollector callGraphCollector;
    private final MutationCollector mutationCollector;
    private final DisjointMutantEnricher disjointMutantEnricher;
    private final List<MetricCollector> metricCollectors;
    private final Writer writer;

    public void calculate() {
        log.info("Collecting dataset(s).");
        Set<DataSet> dataSets = targetCollector.collect()
            .stream()
            .map(this::collectDataSets)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());

        log.info("Calculating disjoint mutation(s).");
        Set<DataSet> enrichedDataSets = disjointMutantEnricher.enrich(dataSets);

        log.info("Collecting metric(s).");
        List<MetricSet> metricSets = enrichedDataSets.stream()
            .map(this::collectMetrics)
            .collect(Collectors.toList());

        log.info("Applying writer.");
        writer.write(metricSets);

        log.info("Metric collection finished.");
    }

    @SneakyThrows
    private Set<DataSet> collectDataSets(TargetDirectory targetDirectory) {
        AtomicInteger atomicInteger = new AtomicInteger();
        Set<DataSet> coverage = coverageCollector.collect(targetDirectory);
        return forkJoinPool.submit(() -> coverage.parallelStream()
                .peek(dataSet -> log.info("Collecting test case {}/{} ({}.{})",
                    atomicInteger.incrementAndGet(),
                    coverage.size(),
                    dataSet.getTestCase().getClassName(),
                    dataSet.getTestCase().getMethodName()))
                .map(dataSet -> callGraphCollector.collect(targetDirectory, dataSet))
                .map(dataSet -> mutationCollector.collect(targetDirectory, dataSet))
                .collect(Collectors.toSet()))
            .get();
    }

    private MetricSet collectMetrics(DataSet dataSet) {
        return MetricSet.builder()
            .testCase(dataSet.getTestCase())
            .metrics(metricCollectors.stream()
                .map(metricCollector -> metricCollector.collect(dataSet))
                .collect(Collectors.toList()))
            .build();
    }
}
