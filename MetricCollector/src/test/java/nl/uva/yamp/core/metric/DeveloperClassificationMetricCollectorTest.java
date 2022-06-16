package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DeveloperClassificationMetricCollectorTest {

    private final DeveloperClassificationMetricCollector sut = new DeveloperClassificationMetricCollector();

    @ParameterizedTest
    @ValueSource(strings = {"FunctionIntegrationTest", "Functionintegrationtest", "FunctionIT", "FunctionITest"})
    void whenIntegrationTest_expectIT(String className) {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .testCase(CoreTestData.testCaseBuilder()
                .className(className)
                .build())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.stringMetricBuilder()
            .identifier("DEV")
            .value("IT")
            .build());
    }

    @ParameterizedTest
    @ValueSource(strings = {"FunctionTest", "Functiontest"})
    void whenUnitTest_expectUT(String className) {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .testCase(CoreTestData.testCaseBuilder()
                .className(className)
                .build())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.stringMetricBuilder()
            .identifier("DEV")
            .value("UT")
            .build());
    }

    @ParameterizedTest
    @ValueSource(strings = {"FunctionVerifier", "FunctionSetup"})
    void whenUnknownType_expectNA(String className) {
        DataSet dataSet = CoreTestData.dataSetBuilder()
            .testCase(CoreTestData.testCaseBuilder()
                .className(className)
                .build())
            .build();

        Metric result = sut.collect(dataSet);

        assertThat(result).isEqualTo(CoreTestData.stringMetricBuilder()
            .identifier("DEV")
            .value("NA")
            .build());
    }
}