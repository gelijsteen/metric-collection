package nl.uva.yamp.core.filter;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.CombinedData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterTest {

    @Spy
    private Filter firstFilter;

    @Spy
    private Filter secondFilter;

    @Test
    void whenAndThenCalled_expectApplyCalledInOrder() {
        Filter sut = firstFilter.andThen(secondFilter);
        CombinedData combinedData1 = CoreTestData.combinedDataBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("coverage1")
                .build())
            .build();
        CombinedData combinedData2 = CoreTestData.combinedDataBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("coverage2")
                .build())
            .build();
        CombinedData combinedData3 = CoreTestData.combinedDataBuilder()
            .testMethod(CoreTestData.methodBuilder()
                .methodName("coverage3")
                .build())
            .build();
        when(firstFilter.apply(combinedData1)).thenReturn(combinedData2);
        when(secondFilter.apply(combinedData2)).thenReturn(combinedData3);

        CombinedData result = sut.apply(combinedData1);

        InOrder inOrder = inOrder(firstFilter, secondFilter);
        inOrder.verify(firstFilter).apply(combinedData1);
        inOrder.verify(secondFilter).apply(combinedData2);
        assertThat(result).isEqualTo(combinedData3);
    }
}