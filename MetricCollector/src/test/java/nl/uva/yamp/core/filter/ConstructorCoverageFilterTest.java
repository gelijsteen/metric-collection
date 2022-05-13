package nl.uva.yamp.core.filter;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.Coverage;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ConstructorCoverageFilterTest {

    private final ConstructorCoverageFilter sut = new ConstructorCoverageFilter();

    @Test
    void whenConstructorPresent_expectConstructorFiltered() {
        Coverage result = sut.apply(CoreTestData.coverageBuilder()
            .coveredMethods(Set.of(
                CoreTestData.methodBuilder().build(),
                CoreTestData.methodBuilder()
                    .methodName("<init>")
                    .build()))
            .build());

        assertThat(result).isEqualTo(CoreTestData.coverageBuilder()
            .coveredMethods(Set.of(CoreTestData.methodBuilder().build()))
            .build());
    }
}