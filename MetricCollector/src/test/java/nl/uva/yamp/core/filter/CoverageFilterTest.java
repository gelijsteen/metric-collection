package nl.uva.yamp.core.filter;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.Coverage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoverageFilterTest {

    @Spy
    private CoverageFilter firstFilter;

    @Spy
    private CoverageFilter secondFilter;

    @Test
    void whenAndThenCalled_expectApplyCalledInOrder() {
        CoverageFilter sut = firstFilter.andThen(secondFilter);
        Coverage coverage1 = CoreTestData.coverageBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("coverage1")
                .build())
            .build();
        Coverage coverage2 = CoreTestData.coverageBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("coverage2")
                .build())
            .build();
        Coverage coverage3 = CoreTestData.coverageBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("coverage3")
                .build())
            .build();
        when(firstFilter.apply(coverage1)).thenReturn(coverage2);
        when(secondFilter.apply(coverage2)).thenReturn(coverage3);

        Coverage result = sut.apply(coverage1);

        InOrder inOrder = inOrder(firstFilter, secondFilter);
        inOrder.verify(firstFilter).apply(coverage1);
        inOrder.verify(secondFilter).apply(coverage2);
        assertThat(result).isEqualTo(coverage3);
    }
}