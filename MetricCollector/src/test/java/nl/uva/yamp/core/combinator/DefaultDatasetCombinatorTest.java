package nl.uva.yamp.core.combinator;

import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.CombinedData;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultDatasetCombinatorTest {

    private final DefaultDatasetCombinator sut = new DefaultDatasetCombinator();

    @Test
    void happyFlow() {
        Set<CombinedData> result = sut.combine(
            Set.of(CoreTestData.coverageBuilder().build()),
            Set.of(CoreTestData.mutationBuilder().build())
        );

        assertThat(result).containsExactly(CoreTestData.combinedDataBuilder().build());
    }
}