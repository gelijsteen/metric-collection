package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.metric.Metric;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class IndirectPackagesCoveredMetricCollectorTest {

    private final IndirectPackagesCoveredMetricCollector sut = new IndirectPackagesCoveredMetricCollector();

    @Test
    void whenEmptyCoveredMethods_expectZero() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Collections.emptySet())
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IPC")
            .value(0)
            .build());
    }

    @Test
    void whenSingleCoveredMethod_expectOne() {
        Coverage coverage = CoreTestData.coverageBuilder().build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IPC")
            .value(1)
            .build());
    }

    @Test
    void whenMultipleMethodsInPackage_expectOne() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder().build(),
                CoreTestData.methodBuilder()
                    .methodName("unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IPC")
            .value(1)
            .build());
    }

    @Test
    void whenTwoDistinctPackages_expectTwo() {
        Coverage coverage = CoreTestData.coverageBuilder()
            .methods(Set.of(
                CoreTestData.methodBuilder().build(),
                CoreTestData.methodBuilder()
                    .packageName("Unique")
                    .build()
            ))
            .build();

        Metric result = sut.collect(coverage);

        assertThat(result).isEqualTo(CoreTestData.integerMetricBuilder()
            .identifier("IPC")
            .value(2)
            .build());
    }
}