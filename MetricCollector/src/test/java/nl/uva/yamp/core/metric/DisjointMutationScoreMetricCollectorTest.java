package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DisjointMutationScoreMetricCollectorTest {

    private final DisjointMutationScoreMetricCollector sut = new DisjointMutationScoreMetricCollector();

    @Test
    void whenEmptyMutations_expectZero() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .mutations(Set.of())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("dMutationScore")
            .value(0d)
            .build());
    }

    @Test
    void whenNonDisjointMutation_expectZero() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .mutations(Set.of(
                CoreTestData.mutationBuilder()
                    .disjoint(false)
                    .build()
            ))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("dMutationScore")
            .value(0d)
            .build());
    }

    @Test
    void whenTwoDisjointMutations_expectValidScore() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .mutations(Set.of(
                CoreTestData.mutationBuilder()
                    .lineNumber(5)
                    .killed(false)
                    .disjoint(true)
                    .build(),
                CoreTestData.mutationBuilder()
                    .killed(true)
                    .disjoint(true)
                    .build()
            ))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("dMutationScore")
            .value(0.5)
            .build());
    }
}