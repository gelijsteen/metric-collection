package nl.uva.yamp.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.CallGraphCollector;
import nl.uva.yamp.core.collector.CoverageCollector;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.collector.TargetCollector;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.MetricSet;
import nl.uva.yamp.core.model.TargetDirectory;
import nl.uva.yamp.core.writer.Writer;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MetricCalculation {

    private final TargetCollector targetCollector;
    private final CoverageCollector coverageCollector;
    private final CallGraphCollector callGraphCollector;
    private final MutationCollector mutationCollector;
    private final List<MetricCollector> metricCollectors;
    private final Writer writer;

    public void calculate() {
        log.info("Collecting dataset(s).");
        Set<DataSet> dataSets = targetCollector.collect()
            .stream()
            .flatMap(this::collectDataSets)
            .collect(Collectors.toSet());

        log.info("Collecting metric(s).");
        List<MetricSet> metricSets = dataSets.stream()
            .map(this::collectMetrics)
            .collect(Collectors.toList());

        log.info("Applying writer.");
        writer.write(metricSets);

        log.info("Metric collection finished.");
    }

    private Stream<DataSet> collectDataSets(TargetDirectory targetDirectory) {
        return coverageCollector.collect(targetDirectory)
            .stream()
            .map(dataSet -> callGraphCollector.collect(targetDirectory, dataSet))
            .map(dataSet -> mutationCollector.collect(targetDirectory, dataSet));
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
