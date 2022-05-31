package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class ApplicationLocMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("aLOC")
            .value(calculate(dataSet))
            .build();
    }

    private int calculate(DataSet dataSet) {
        int constructorLoc = dataSet.getConstructors().stream()
            .mapToInt(Constructor::getLoc)
            .sum();
        int methodLoc = dataSet.getMethods().stream()
            .mapToInt(Method::getLoc)
            .sum();
        return methodLoc + constructorLoc;
    }
}
