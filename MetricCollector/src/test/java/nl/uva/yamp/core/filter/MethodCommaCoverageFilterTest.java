package nl.uva.yamp.core.filter;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.Coverage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodCommaCoverageFilterTest {

    private final MethodCommaCoverageFilter sut = new MethodCommaCoverageFilter();

    @Test
    void whenNoCommaOrNewline_expectNoModifications() {
        Coverage result = sut.apply(CoreTestData.coverageBuilder().build());

        assertThat(result).isEqualTo(CoreTestData.coverageBuilder().build());
    }

    @Test
    void whenComma_expectReplacement() {
        Coverage result = sut.apply(CoreTestData.coverageBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("method[a=1,b=2]")
                .build())
            .build());

        assertThat(result).isEqualTo(CoreTestData.coverageBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("method[a=1;b=2]")
                .build())
            .build());
    }

    @Test
    void whenNewline_expectReplacement() {
        Coverage result = sut.apply(CoreTestData.coverageBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("method[a=1\nb=2]")
                .build())
            .build());

        assertThat(result).isEqualTo(CoreTestData.coverageBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("method[a=1;b=2]")
                .build())
            .build());
    }

    @Test
    void whenCommaAndNewline_expectReplacements() {
        Coverage result = sut.apply(CoreTestData.coverageBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("method[a=1,b=2\nc=3]")
                .build())
            .build());

        assertThat(result).isEqualTo(CoreTestData.coverageBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("method[a=1;b=2;c=3]")
                .build())
            .build());
    }
}