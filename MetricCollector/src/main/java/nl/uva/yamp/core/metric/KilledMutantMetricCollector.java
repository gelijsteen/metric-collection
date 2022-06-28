package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class KilledMutantMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("NKM")
            .value(calculate(dataSet))
            .build();
    }

    private int calculate(DataSet dataSet) {
        return (int) dataSet.getMutations()
            .stream()
            .filter(Mutation::getKilled)
            .count();
    }
}
