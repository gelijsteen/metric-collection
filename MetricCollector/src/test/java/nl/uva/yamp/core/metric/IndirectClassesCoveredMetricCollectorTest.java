package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class IndirectClassesCoveredMetricCollectorTest {

    private final IndirectClassesCoveredMetricCollector sut = new IndirectClassesCoveredMetricCollector();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("ICC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredMethod_expectOne() {
        Coverage coverage = CoreTestData.coverageBuilder().build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("ICC")
            .value(1)
            .build());
    }

    @Test
    void whenMultipleMethodsInClass_expectOne() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder().build(),
                CoreTestData.methodBuilder()
                    .methodName("unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("ICC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctClasses_expectTwo() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder().build(),
                CoreTestData.methodBuilder()
                    .className("Unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("ICC")
            .value(2)
            .build());
    }
}