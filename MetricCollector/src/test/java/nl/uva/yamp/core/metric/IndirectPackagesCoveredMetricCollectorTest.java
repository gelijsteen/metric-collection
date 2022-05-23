package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class IndirectPackagesCoveredMetricCollectorTest {

    private final IndirectPackagesCoveredMetricCollector sut = new IndirectPackagesCoveredMetricCollector();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IPC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredMethod_expectOne() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder().build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IPC")
            .value(1)
            .build());
    }

    @Test
    void whenMultipleMethodsInPackage_expectOne() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Set.of(
                CoreTestData.combinedMethodBuilder().build(),
                CoreTestData.combinedMethodBuilder()
                    .methodName("unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IPC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctPackages_expectTwo() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Set.of(
                CoreTestData.combinedMethodBuilder().build(),
                CoreTestData.combinedMethodBuilder()
                    .packageName("Unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IPC")
            .value(2)
            .build());
    }
}