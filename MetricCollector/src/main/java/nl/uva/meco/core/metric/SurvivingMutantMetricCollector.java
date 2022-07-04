package nl.uva.meco.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.metric.LongMetric;
import nl.uva.meco.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class SurvivingMutantMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return LongMetric.builder()
            .identifier("NSM")
            .value(calculate(dataSet))
            .build();
    }

    private long calculate(DataSet dataSet) {
        return dataSet.getMutations()
            .stream()
            .filter(m -> !m.getKilled())
            .count();
    }
}
