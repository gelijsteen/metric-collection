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
public class MutationScoreMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return DoubleMetric.builder()
            .identifier("MutationScore")
            .value(getMutationScore(dataSet))
            .build();
    }

    private double getMutationScore(DataSet dataSet) {
        Set<Mutation> allMutations = dataSet.getMutations();
        Set<Mutation> killedMutations = allMutations.stream()
            .filter(Mutation::getKilled)
            .collect(Collectors.toSet());
        return (killedMutations.size() * 1d) / allMutations.size();
    }
}
