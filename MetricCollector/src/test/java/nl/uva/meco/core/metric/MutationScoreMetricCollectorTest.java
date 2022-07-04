package nl.uva.meco.core.metric;

import nl.uva.meco.core.CoreTestData;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MutationScoreMetricCollectorTest {

    private final MutationScoreMetricCollector sut = new MutationScoreMetricCollector();

    @Test
    void whenEmptyMutations_expectZero() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .mutations(Set.of())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("MutationScore")
            .value(0d)
            .build());
    }

    @Test
    void whenOneKilledOneSurvivedMutation_expectValidScore() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .mutations(Set.of(
                CoreTestData.mutationBuilder()
                    .lineNumber(5)
                    .killed(false)
                    .build(),
                CoreTestData.mutationBuilder()
                    .killed(true)
                    .build()
            ))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("MutationScore")
            .value(0.5)
            .build());
    }
}