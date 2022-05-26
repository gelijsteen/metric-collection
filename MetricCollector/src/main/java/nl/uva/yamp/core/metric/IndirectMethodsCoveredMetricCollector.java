package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class IndirectMethodsCoveredMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("IMC")
            .value(calculate(dataSet))
            .build();
    }

    private int calculate(DataSet dataSet) {
        return dataSet.getMethods()
            .stream()
            .map(Method::getSignature)
            .collect(Collectors.toSet())
            .size();
    }
}
