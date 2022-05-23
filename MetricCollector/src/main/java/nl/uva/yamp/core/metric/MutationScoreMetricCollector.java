package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.metric.DoubleMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class MutationScoreMetricCollector implements MetricCollector {

    @Override
    public Metric collect(CombinedData combinedData) {
        return DoubleMetric.builder()
            .identifier("MutationScore")
            .value(combinedData.getMutationScore() / 100d)
            .build();
    }
}
