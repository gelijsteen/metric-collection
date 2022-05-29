package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MutationScoreMetricCollectorTest {

    private final MutationScoreMetricCollector sut = new MutationScoreMetricCollector();

    @Test
    void happyFlow() {
        Metric result = sut.collect(CoreTestData.dataSetBuilder()
            .mutations(Set.of(
                CoreTestData.mutationBuilder()
                    .lineNumber(5)
                    .killed(false)
                    .build(),
                CoreTestData.mutationBuilder()
                    .killed(true)
                    .build()
            ))
            .build());

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("MutationScore")
            .value(0.5)
            .build());
    }
}