package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UniqueMethodsMetricCollectorTest {

    private final UniqueMethodsMetricCollector sut = new UniqueMethodsMetricCollector();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .coveredMethods(Collections.emptySet())
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(IntegerMetric.builder()
            .identifier("UMC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredMethod_expectOne() {
        Coverage coverage = CoreTestData.coverageBuilder().build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(IntegerMetric.builder()
            .identifier("UMC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctMethods_expectTwo() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .coveredMethods(Set.of(
                CoreTestData.methodBuilder().build(),
                CoreTestData.methodBuilder()
                    .methodName("Unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(IntegerMetric.builder()
            .identifier("UMC")
            .value(2)
            .build());
    }
}