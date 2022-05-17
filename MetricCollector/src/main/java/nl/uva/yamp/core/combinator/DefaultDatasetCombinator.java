package nl.uva.yamp.core.combinator;

import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.Mutation;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultDatasetCombinator implements DatasetCombinator {

    @Override
    public Set<CombinedData> combine(Set<Coverage> coverageData, Set<Mutation> mutationData) {
        Map<Method, Set<Method>> collect = coverageData.stream()
            .collect(Collectors.toMap(Coverage::getTestMethod, Coverage::getCoveredMethods));
        return mutationData.stream()
            .map(mutation -> CombinedData.builder()
                .testMethod(mutation.getTestMethod())
                .coveredMethods(collect.get(mutation.getTestMethod()))
                .mutationScore(mutation.getMutationScore())
                .build())
            .collect(Collectors.toSet());
    }
}
