package nl.uva.yamp.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.CallGraphCollector;
import nl.uva.yamp.core.collector.CoverageCollector;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.metric.TestMetrics;
import nl.uva.yamp.core.writer.Writer;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MetricCalculation {

    private final CoverageCollector coverageCollector;
    private final CallGraphCollector callGraphCollector;
    private final MutationCollector mutationCollector;
    private final List<MetricCollector> metricCollectors;
    private final Writer writer;

    public void calculate() {
        log.info("Collecting coverage data.");
        Set<Coverage> coverages = coverageCollector.collect();

        log.info("Collecting call graph data.");
        Set<Coverage> callGraphs = coverages.stream()
            .map(callGraphCollector::collect)
            .collect(Collectors.toSet());

        log.info("Collecting mutation data.");
        Set<Coverage> mutations = callGraphs.stream()
            .map(mutationCollector::collect)
            .collect(Collectors.toSet());

        log.info("Collecting metric(s).");
        List<TestMetrics> testMetrics = mutations.stream()
            .map(this::collectMetrics)
            .collect(Collectors.toList());

        log.info("Applying writer.");
        writer.write(testMetrics);

        log.info("Metric collection finished.");
    }

    private TestMetrics collectMetrics(Coverage combinedData) {
        return TestMetrics.builder()
            .testCase(combinedData.getTestCase())
            .metrics(metricCollectors.stream()
                .map(metricCollector -> metricCollector.collect(combinedData))
                .collect(Collectors.toList()))
            .build();
    }
}
