package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class KilledMutantMetricCollectorTest {

    private final KilledMutantMetricCollector sut = new KilledMutantMetricCollector();

    @Test
    void whenEmptyMutations_expectZero() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .mutations(Set.of())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("NKM")
            .value(0)
            .build());
    }

    @Test
    void whenNonEmptyMutations_expectValidScore() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .mutations(Set.of(
                CoreTestData.mutationBuilder()
                    .lineNumber(1)
                    .build(),
                CoreTestData.mutationBuilder()
                    .lineNumber(2)
                    .build(),
                CoreTestData.mutationBuilder()
                    .lineNumber(3)
                    .killed(true)
                    .build(),
                CoreTestData.mutationBuilder()
                    .lineNumber(4)
                    .killed(true)
                    .build(),
                CoreTestData.mutationBuilder()
                    .lineNumber(5)
                    .disjoint(true)
                    .build(),
                CoreTestData.mutationBuilder()
                    .lineNumber(6)
                    .disjoint(true)
                    .build(),
                CoreTestData.mutationBuilder()
                    .lineNumber(7)
                    .killed(true)
                    .disjoint(true)
                    .build(),
                CoreTestData.mutationBuilder()
                    .lineNumber(8)
                    .killed(true)
                    .disjoint(true)
                    .build()
            ))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("NKM")
            .value(4)
            .build());
    }
}