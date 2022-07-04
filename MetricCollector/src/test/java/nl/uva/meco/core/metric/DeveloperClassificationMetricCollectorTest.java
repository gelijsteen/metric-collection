package nl.uva.meco.core.metric;

import nl.uva.meco.core.CoreTestData;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.metric.Metric;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DeveloperClassificationMetricCollectorTest {

    private final DeveloperClassificationMetricCollector sut = new DeveloperClassificationMetricCollector();

    @ParameterizedTest
    @ValueSource(strings = {"FunctionTest", "Functiontest", "testFunction", "TestFunction"})
    void whenUnitTest_expectOne(String className) {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .testCase(CoreTestData.testCaseBuilder()
                .className(className)
                .build())
            .methods(Set.of(CoreTestData.methodBuilder()
                .className("Function")
                .build()))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("UT")
            .value(1)
            .build());
    }

    @ParameterizedTest
    @ValueSource(strings = {"FunctionIntegrationTest", "Functionintegrationtest", "FunctionIT", "FunctionITest"})
    void whenNonUnitTest_expectZero(String className) {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .testCase(CoreTestData.testCaseBuilder()
                .className(className)
                .build())
            .methods(Set.of(CoreTestData.methodBuilder()
                .className("Function")
                .build()))
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("UT")
            .value(0)
            .build());
    }
}