package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class IndirectClassesCoveredMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("ICC")
            .value(calculate(dataSet))
            .build();
    }

    private int calculate(DataSet dataSet) {
        return dataSet.getMethods()
            .stream()
            .map(Method::getFullyQualifiedClassName)
            .collect(Collectors.toSet())
            .size();
    }
}
