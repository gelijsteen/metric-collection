package nl.uva.meco.core.metric;

import nl.uva.meco.core.CoreTestData;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TestLocMetricCollectorTest {

    private final TestLocMetricCollector sut = new TestLocMetricCollector();

    @Test
    void whenMissingTestData_expectZero() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .testConstructors(Set.of())
            .testMethods(Set.of())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("tLOC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleTestConstructor_expectOne() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .testMethods(Set.of())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("tLOC")
            .value(1)
            .build());
    }

    @Test
    void whenSingleTestMethod_expectOne() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .testConstructors(Set.of())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("tLOC")
            .value(1)
            .build());
    }

    @Test
    void whenBothTestConstructorAndMethod_expectTwo() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("tLOC")
            .value(2)
            .build());
    }

    @Test
    void whenTwoLocForConstructorAndMethod_expectFour() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .testConstructors(Set.of(
                CoreTestData.constructorBuilder()
                    .loc(2)
                    .build()
            ))
            .testMethods(Set.of(
                CoreTestData.methodBuilder()
                    .loc(2)
                    .build()
            ))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("tLOC")
            .value(4)
            .build());
    }
}