package nl.uva.yamp.core.combinator;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.TestCase;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DefaultDatasetCombinator implements DatasetCombinator {

    @Override
    public Set<CombinedData> combine(Set<Coverage> coverageData, Set<Mutation> mutationData) {
        Map<TestCase, Coverage> coverageMap = coverageData.stream()
            .collect(Collectors.toMap(Coverage::getTestCase, coverage -> coverage));
        return mutationData.stream()
            .map(mutation -> CombinedData.builder()
                .testCase(mutation.getTestCase())
                .constructors(coverageMap.get(mutation.getTestCase()).getConstructors())
                .methods(coverageMap.get(mutation.getTestCase()).getMethods())
                .mutationScore(mutation.getMutationScore())
                .build())
            .collect(Collectors.toSet());
    }
}
