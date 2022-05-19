package nl.uva.yamp.core.combinator;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.Mutation;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DefaultDatasetCombinator implements DatasetCombinator {

    @Override
    public Set<CombinedData> combine(Set<Coverage> coverageData, Set<Mutation> mutationData) {
        Map<Method, Coverage> coverageMap = coverageData.stream()
            .collect(Collectors.toMap(Coverage::getTestMethod, coverage -> coverage));
        return mutationData.stream()
            .map(mutation -> CombinedData.builder()
                .testMethod(mutation.getTestMethod())
                .constructors(coverageMap.get(mutation.getTestMethod()).getConstructors())
                .methods(coverageMap.get(mutation.getTestMethod()).getMethods())
                .mutationScore(mutation.getMutationScore())
                .build())
            .collect(Collectors.toSet());
    }
}
