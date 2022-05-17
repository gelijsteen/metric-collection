package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UniquePackagesMetricCollectorTest {

    private final UniquePackagesMetricCollector sut = new UniquePackagesMetricCollector();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(IntegerMetric.builder()
            .identifier("UPC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredMethod_expectOne() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder().build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(IntegerMetric.builder()
            .identifier("UPC")
            .value(1)
            .build());
    }

    @Test
    void whenMultipleMethodsInPackage_expectOne() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder().build(),
                CoreTestData.methodBuilder()
                    .methodName("unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(IntegerMetric.builder()
            .identifier("UPC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctPackages_expectTwo() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder().build(),
                CoreTestData.methodBuilder()
                    .packageName("Unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(IntegerMetric.builder()
            .identifier("UPC")
            .value(2)
            .build());
    }
}