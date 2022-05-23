package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DistinctPackageHierarchiesCoveredMetricCollectorTest {

    private final DistinctPackageHierarchiesCoveredMetricCollector sut = new DistinctPackageHierarchiesCoveredMetricCollector();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("DPHC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredMethod_expectOne() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("DPHC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoNestedPackages_expectOne() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Set.of(
                CoreTestData.combinedMethodBuilder()
                    .packageName("one.two")
                    .build(),
                CoreTestData.combinedMethodBuilder()
                    .packageName("one.two.three")
                    .build()))
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("DPHC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctPackages_expectTwo() {
        CombinedData combinedData = CoreTestData.combinedDataBuilder()
            .methods(Set.of(
                CoreTestData.combinedMethodBuilder()
                    .packageName("one.two")
                    .build(),
                CoreTestData.combinedMethodBuilder()
                    .packageName("alpha.beta")
                    .build()))
            .build();

        Metric result = sut.collect(combinedData);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("DPHC")
            .value(2)
            .build());
    }
}