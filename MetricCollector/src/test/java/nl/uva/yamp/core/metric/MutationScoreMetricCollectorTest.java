package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MutationScoreMetricCollectorTest {

    private final MutationScoreMetricCollector sut = new MutationScoreMetricCollector();

    @Test
    void happyFlow() {
        Metric result = sut.collect(CoreTestData.coverageBuilder().build());

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("MutationScore")
            .value(1d)
            .build());
    }
}