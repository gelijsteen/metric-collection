package nl.uva.meco.core.metric;

import nl.uva.meco.core.CoreTestData;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RecursiveDirectnessMetricCalculatorTest {

    private final RecursiveDirectnessMetricCalculator sut = new RecursiveDirectnessMetricCalculator();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("rDirectness")
            .value(0d)
            .build());
    }

    @Test
    void whenDirectAndIndirectEqual_expectOne() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder()
                    .loc(5)
                    .build()
            ))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("rDirectness")
            .value(1d)
            .build());
    }

    @Test
    void whenDirectAndIndirectInequal_expectFraction() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder()
                    .loc(5)
                    .build(),
                CoreTestData.methodBuilder()
                    .methodName("alternative")
                    .loc(10)
                    .direct(false)
                    .build()
            ))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.doubleMetricBuilder()
            .identifier("rDirectness")
            .value(1d / 3)
            .build());
    }
}