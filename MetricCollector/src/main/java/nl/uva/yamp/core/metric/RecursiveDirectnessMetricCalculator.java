package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.CombinedMethod;
import nl.uva.yamp.core.model.metric.DoubleMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class RecursiveDirectnessMetricCalculator implements MetricCollector {

    @Override
    public Metric collect(CombinedData combinedData) {
        return DoubleMetric.builder()
            .identifier("rDirectness")
            .value(calculate(combinedData))
            .build();
    }

    private double calculate(CombinedData combinedData) {
        double directLoc = combinedData.getMethods().stream()
            .mapToDouble(CombinedMethod::getLoc)
            .sum();
        double indirectLoc = combinedData.getIndirectMethods().stream()
            .mapToDouble(CombinedMethod::getLoc)
            .sum();
        return indirectLoc == 0 ? 0 : directLoc / indirectLoc;
    }
}
