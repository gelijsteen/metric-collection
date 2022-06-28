package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.metric.DoubleMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DisjointMutationScoreMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return DoubleMetric.builder()
            .identifier("dMutationScore")
            .value(calculate(dataSet))
            .build();
    }

    private double calculate(DataSet dataSet) {
        Set<Mutation> allMutations = dataSet.getMutations().stream()
            .filter(Mutation::getDisjoint)
            .collect(Collectors.toSet());
        Set<Mutation> killedMutations = allMutations.stream()
            .filter(Mutation::getKilled)
            .collect(Collectors.toSet());
        return allMutations.isEmpty() ? 0 : (killedMutations.size() * 1d) / allMutations.size();
    }
}
