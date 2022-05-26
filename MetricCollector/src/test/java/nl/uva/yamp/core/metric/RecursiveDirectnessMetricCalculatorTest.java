package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RecursiveDirectnessMetricCalculatorTest {

    private final RecursiveDirectnessMetricCalculator sut = new RecursiveDirectnessMetricCalculator();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("rDirectness")
            .value(0d)
            .build());
    }

    @Test
    void whenDirectAndIndirectEqual_expectOne() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder()
                    .loc(5)
                    .direct(true)
                    .build()
            ))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("rDirectness")
            .value(1d)
            .build());
    }

    @Test
    void whenDirectAndIndirectInequal_expectFraction() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder()
                    .loc(5)
                    .direct(true)
                    .build(),
                CoreTestData.methodBuilder()
                    .methodName("alternative")
                    .loc(10)
                    .build()
            ))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("rDirectness")
            .value(1d / 3)
            .build());
    }
}