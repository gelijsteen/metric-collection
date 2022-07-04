package nl.uva.meco.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.Mutation;
import nl.uva.meco.core.model.metric.LongMetric;
import nl.uva.meco.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class KilledMutantMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return LongMetric.builder()
            .identifier("NKM")
            .value(calculate(dataSet))
            .build();
    }

    private long calculate(DataSet dataSet) {
        return dataSet.getMutations()
            .stream()
            .filter(Mutation::getKilled)
            .count();
    }
}
