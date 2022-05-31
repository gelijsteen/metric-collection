package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationLocMetricCollectorTest {

    private final ApplicationLocMetricCollector sut = new ApplicationLocMetricCollector();

    @Test
    void whenMissingData_expectZero() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .constructors(Set.of())
            .methods(Set.of())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("aLOC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleConstructor_expectOne() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .methods(Set.of())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("aLOC")
            .value(1)
            .build());
    }

    @Test
    void whenSingleMethod_expectOne() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .constructors(Set.of())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("aLOC")
            .value(1)
            .build());
    }

    @Test
    void whenBothConstructorAndMethod_expectTwo() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("aLOC")
            .value(2)
            .build());
    }

    @Test
    void whenTwoLocForConstructorAndMethod_expectFour() {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .constructors(Set.of(
                CoreTestData.constructorBuilder()
                    .loc(2)
                    .build()
            ))
            .methods(Set.of(
                CoreTestData.methodBuilder()
                    .loc(2)
                    .build()
            ))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("aLOC")
            .value(4)
            .build());
    }
}