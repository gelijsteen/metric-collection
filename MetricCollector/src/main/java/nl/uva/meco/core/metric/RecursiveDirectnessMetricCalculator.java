package nl.uva.meco.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.Method;
import nl.uva.meco.core.model.metric.DoubleMetric;
import nl.uva.meco.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class RecursiveDirectnessMetricCalculator implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return DoubleMetric.builder()
            .identifier("rDirectness")
            .value(calculate(dataSet))
            .build();
    }

    private double calculate(DataSet dataSet) {
        double directLoc = dataSet.getMethods().stream()
            .filter(Method::getDirect)
            .mapToDouble(Method::getLoc)
            .sum();
        double indirectLoc = dataSet.getMethods().stream()
            .mapToDouble(Method::getLoc)
            .sum();
        return indirectLoc == 0 ? 0 : directLoc / indirectLoc;
    }
}
