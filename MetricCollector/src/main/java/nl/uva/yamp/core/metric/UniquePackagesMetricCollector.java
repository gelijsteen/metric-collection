package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.metric.Metric;

import java.util.stream.Collectors;

public class UniquePackagesMetricCollector implements MetricCollector {

    @Override
    public Metric collect(Coverage coverage) {
        return IntegerMetric.builder()
            .identifier("UPC")
            .value(calculate(coverage))
            .build();
    }

    private int calculate(Coverage coverage) {
        return coverage.getCoveredMethods()
            .stream()
            .map(Method::getPackageName)
            .collect(Collectors.toSet())
            .size();
    }
}
