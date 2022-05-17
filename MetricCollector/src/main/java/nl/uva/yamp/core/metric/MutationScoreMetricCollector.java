package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

public class MutationScoreMetricCollector implements MetricCollector {

    @Override
    public Metric collect(CombinedData combinedData) {
        return IntegerMetric.builder()
            .identifier("MutationScore")
            .value(combinedData.getMutationScore())
            .build();
    }
}
