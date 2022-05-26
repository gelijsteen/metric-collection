package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RecursiveTdataMetricCollectorTest {

    private final RecursiveTdataMetricCollector sut = new RecursiveTdataMetricCollector();

    @Test
    void whenEmptyCoveredConstructors_expectZero() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .constructors(Collections.emptySet())
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("rTDATA")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredConstructor_expectOne() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .constructors(Set.of(
                CoreTestData.constructorBuilder()
                    .direct(true)
                    .build()
            ))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("rTDATA")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctConstructors_expectTwo() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .constructors(Set.of(
                CoreTestData.constructorBuilder()
                    .direct(true)
                    .build(),
                CoreTestData.constructorBuilder()
                    .className("Unique")
                    .direct(true)
                    .build()
            ))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("rTDATA")
            .value(2)
            .build());
    }
}