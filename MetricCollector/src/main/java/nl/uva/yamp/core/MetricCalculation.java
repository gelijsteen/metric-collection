package nl.uva.yamp.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.CallGraphCollector;
import nl.uva.yamp.core.collector.CoverageCollector;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.MetricSet;
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
        log.info("Collecting data.");
        Set<DataSet> dataSets = coverageCollector.collect()
            .stream()
            .map(callGraphCollector::collect)
            .map(mutationCollector::collect)
            .collect(Collectors.toSet());

        log.info("Collecting metric(s).");
        List<MetricSet> metricSets = dataSets.stream()
            .map(this::collectMetrics)
            .collect(Collectors.toList());

        log.info("Applying writer.");
        writer.write(metricSets);

        log.info("Metric collection finished.");
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
