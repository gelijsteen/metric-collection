package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MutationScoreMetricCollectorTest {

    private final MutationScoreMetricCollector sut = new MutationScoreMetricCollector();

    @Test
    void happyFlow() {
        Metric result = sut.collect(CoreTestData.combinedDataBuilder().build());

        assertThat(result).isEqualTo(IntegerMetric.builder()
            .identifier("MutationScore")
            .value(100)
            .build());
    }
}