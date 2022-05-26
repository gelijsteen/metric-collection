package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DistinctPackageHierarchiesCoveredMetricCollectorTest {

    private final DistinctPackageHierarchiesCoveredMetricCollector sut = new DistinctPackageHierarchiesCoveredMetricCollector();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("DPHC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredMethod_expectOne() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("DPHC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoNestedPackages_expectOne() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder()
                    .packageName("one.two")
                    .build(),
                CoreTestData.methodBuilder()
                    .packageName("one.two.three")
                    .build()))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("DPHC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctPackages_expectTwo() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder()
                    .packageName("one.two")
                    .build(),
                CoreTestData.methodBuilder()
                    .packageName("alpha.beta")
                    .build()))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("DPHC")
            .value(2)
            .build());
    }
}