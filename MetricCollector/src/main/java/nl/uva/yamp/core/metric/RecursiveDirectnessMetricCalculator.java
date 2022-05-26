package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.metric.DoubleMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class RecursiveDirectnessMetricCalculator implements MetricCollector {

    @Override
    public Metric collect(Coverage combinedData) {
        return DoubleMetric.builder()
            .identifier("rDirectness")
            .value(calculate(combinedData))
            .build();
    }

    private double calculate(Coverage combinedData) {
        double directLoc = combinedData.getMethods().stream()
            .filter(Method::getDirect)
            .mapToDouble(Method::getLoc)
            .sum();
        double indirectLoc = combinedData.getMethods().stream()
            .mapToDouble(Method::getLoc)
            .sum();
        return indirectLoc == 0 ? 0 : directLoc / indirectLoc;
    }
}
