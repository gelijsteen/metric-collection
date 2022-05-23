package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RecursiveDirectnessMetricCalculatorTest {

    private final RecursiveDirectnessMetricCalculator sut = new RecursiveDirectnessMetricCalculator();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("rDirectness")
            .value(0d)
            .build());
    }

    @Test
    void whenDirectAndIndirectEqual_expectOne() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Set.of(CoreTestData.combinedMethodBuilder()
                .loc(5)
                .build()))
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("rDirectness")
            .value(1d)
            .build());
    }

    @Test
    void whenDirectAndIndirectInequal_expectFraction() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Set.of(CoreTestData.combinedMethodBuilder()
                .methods(Set.of(CoreTestData.combinedMethodBuilder()
                    .loc(10)
                    .build()))
                .loc(5)
                .build()))
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("rDirectness")
            .value(1d / 3)
            .build());
    }
}