package nl.uva.meco.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.meco.core.model.Constructor;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.Method;
import nl.uva.meco.core.model.metric.IntegerMetric;
import nl.uva.meco.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class TestLocMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("tLOC")
            .value(calculate(dataSet))
            .build();
    }

    private int calculate(DataSet dataSet) {
        int constructorLoc = dataSet.getTestConstructors().stream()
            .mapToInt(Constructor::getLoc)
            .sum();
        int methodLoc = dataSet.getTestMethods().stream()
            .mapToInt(Method::getLoc)
            .sum();
        return methodLoc + constructorLoc;
    }
}
