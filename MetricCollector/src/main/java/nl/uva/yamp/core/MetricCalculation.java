package nl.uva.yamp.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.callgraph.CallGraphReader;
import nl.uva.yamp.core.combinator.DatasetCombinator;
import nl.uva.yamp.core.coverage.CoverageReader;
import nl.uva.yamp.core.filter.Filter;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.metric.TestMetrics;
import nl.uva.yamp.core.mutation.MutationReader;
import nl.uva.yamp.core.validator.Validator;
import nl.uva.yamp.core.writer.Writer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MetricCalculation {

    private final CoverageReader coverageReader;
    private final CallGraphReader callGraphReader;
    private final MutationReader pitestReader;
    private final Validator validator;
    private final DatasetCombinator datasetCombinator;
    private final List<Filter> coverageFilters;
    private final List<MetricCollector> metricCollectors;
    private final List<Writer> writers;

    public void calculate() {
        log.info("Collecting coverage data.");
        Set<Coverage> coverages = coverageReader.read();

        log.info("Collecting call graph data.");
        Set<CallGraph> callGraphs = coverages.stream()
            .map(callGraphReader::read)
            .collect(Collectors.toSet());

        log.info("Collecting mutation data.");
        Set<Mutation> mutations = coverages.stream()
            .map(pitestReader::read)
            .collect(Collectors.toSet());

        log.info("Applying validator(s).");
        validator.validate(coverages, callGraphs);

        log.info("Combining datasets.");
        Set<CombinedData> combinedData = datasetCombinator.combine(coverages, mutations);

        log.info("Applying filter(s).");
        Filter aggregatedFilters = coverageFilters.stream().reduce(Filter.identity(), Filter::andThen);
        Set<CombinedData> filteredData = combinedData.stream()
            .map(aggregatedFilters::apply)
            .collect(Collectors.toSet());

        log.info("Collecting metric(s).");
        List<TestMetrics> testMetrics = filteredData.stream()
            .map(this::collectMetrics)
            .collect(Collectors.toList());

        log.info("Applying writer(s).");
        writers.forEach(writer -> writer.write(testMetrics));

        log.info("Metric collection finished.");
    }

    private TestMetrics collectMetrics(CombinedData combinedData) {
        return TestMetrics.builder()
            .testMethod(combinedData.getTestMethod())
            .metrics(metricCollectors.stream()
                .map(metricCollector -> metricCollector.collect(combinedData))
                .collect(Collectors.toList()))
            .build();
    }
}
