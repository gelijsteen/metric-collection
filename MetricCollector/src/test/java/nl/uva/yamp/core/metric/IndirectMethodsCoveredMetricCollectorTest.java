package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class IndirectMethodsCoveredMetricCollectorTest {

    private final IndirectMethodsCoveredMetricCollector sut = new IndirectMethodsCoveredMetricCollector();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IMC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredMethod_expectOne() {
        DataSet dataSet = CoreTestData.dataSetBuilder().build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IMC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctMethods_expectTwo() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder().build(),
                CoreTestData.methodBuilder()
                    .methodName("Unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IMC")
            .value(2)
            .build());
    }
}