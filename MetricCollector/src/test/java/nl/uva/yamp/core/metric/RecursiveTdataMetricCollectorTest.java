package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RecursiveTdataMetricCollectorTest {

    private final RecursiveTdataMetricCollector sut = new RecursiveTdataMetricCollector();

    @Test
    void whenEmptyCoveredConstructors_expectZero() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .constructors(Collections.emptySet())
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("rTDATA")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredConstructor_expectOne() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder().build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("rTDATA")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctConstructors_expectTwo() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .constructors(Set.of(
                CoreTestData.combinedConstructorBuilder().build(),
                CoreTestData.combinedConstructorBuilder()
                    .className("Unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("rTDATA")
            .value(2)
            .build());
    }
}