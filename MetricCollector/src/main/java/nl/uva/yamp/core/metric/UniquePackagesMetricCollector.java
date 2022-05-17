package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import java.util.stream.Collectors;

public class UniquePackagesMetricCollector implements MetricCollector {

    @Override
    public Metric collect(CombinedData combinedData) {
        return IntegerMetric.builder()
            .identifier("UPC")
            .value(calculate(combinedData))
            .build();
    }

    private int calculate(CombinedData combinedData) {
        return combinedData.getCoveredMethods()
            .stream()
            .map(Method::getPackageName)
            .collect(Collectors.toSet())
            .size();
    }
}
