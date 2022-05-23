package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.CoverageMethod;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class IndirectClassesCoveredMetricCollector implements MetricCollector {

    @Override
    public Metric collect(CombinedData combinedData) {
        return IntegerMetric.builder()
            .identifier("ICC")
            .value(calculate(combinedData))
            .build();
    }

    private int calculate(CombinedData combinedData) {
        return combinedData.getMethods()
            .stream()
            .map(CoverageMethod::getFullyQualifiedClassName)
            .collect(Collectors.toSet())
            .size();
    }
}