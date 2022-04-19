package nl.uva.yamp.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.filter.CoverageFilter;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.metric.TestMetrics;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MetricCalculation {

    private final Reader reader;
    private final List<CoverageFilter> coverageFilters;
    private final List<MetricCollector> metricCollectors;
    private final List<Writer> writers;

    public void calculate() {
        log.info("Applying reader.");
        Set<Coverage> coverages = reader.read();

        log.info("Applying filter(s).");
        Set<Coverage> filteredCoverages = coverages.stream()
            .map(this::applyFilters)
            .collect(Collectors.toSet());

        log.info("Collecting metric(s).");
        List<TestMetrics> testMetrics = filteredCoverages.stream()
            .map(this::collectMetrics)
            .collect(Collectors.toList());

        log.info("Applying writer(s).");
        writers.forEach(writer -> writer.write(testMetrics));

        log.info("Metric collection finished.");
    }

    private Coverage applyFilters(Coverage coverage) {
        return coverageFilters.stream()
            .reduce(CoverageFilter.identity(), CoverageFilter::andThen)
            .apply(coverage);
    }

    private TestMetrics collectMetrics(Coverage coverage) {
        return TestMetrics.builder()
            .testMethod(coverage.getTestMethod())
            .metrics(metricCollectors.stream()
                .map(metricCollector -> metricCollector.collect(coverage))
                .collect(Collectors.toList()))
            .build();
    }
}
