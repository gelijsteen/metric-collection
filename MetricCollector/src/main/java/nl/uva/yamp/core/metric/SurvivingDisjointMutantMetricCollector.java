package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class SurvivingDisjointMutantMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("dNSM")
            .value(calculate(dataSet))
            .build();
    }

    private int calculate(DataSet dataSet) {
        return (int) dataSet.getMutations()
            .stream()
            .filter(Mutation::getDisjoint)
            .filter(m -> !m.getKilled())
            .count();
    }
}
