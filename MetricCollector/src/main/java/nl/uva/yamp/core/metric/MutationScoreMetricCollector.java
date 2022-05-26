package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.metric.DoubleMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class MutationScoreMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return DoubleMetric.builder()
            .identifier("MutationScore")
            .value(dataSet.getMutationScore() / 100d)
            .build();
    }
}
