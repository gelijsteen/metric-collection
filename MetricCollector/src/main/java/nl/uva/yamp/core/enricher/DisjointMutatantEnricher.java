package nl.uva.yamp.core.enricher;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.TestCase;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(onConstructor = @__(@Inject))
public class DisjointMutatantEnricher {

    public Set<DataSet> enrich(Set<DataSet> dataSets) {
        Map<Mutation, Map<TestCase, Boolean>> matrix = mapToMatrix(dataSets);
        Map<Mutation, Map<TestCase, Boolean>> filteredMatrix = removeLiveMutants(matrix);
        Set<Mutation> disjointMutants = selectDisjointMutants(filteredMatrix);
        return enrichDataSets(dataSets, disjointMutants);
    }

    private Map<Mutation, Map<TestCase, Boolean>> mapToMatrix(Set<DataSet> dataSets) {
        Map<Mutation, Map<TestCase, Boolean>> map = new HashMap<>();
        dataSets.forEach(dataSet ->
            dataSet.getMutations().forEach(mutation ->
                map.computeIfAbsent(mutation, key -> new HashMap<>())
                    .put(dataSet.getTestCase(), mutation.getKilled())));
        return map;
    }

    private Map<Mutation, Map<TestCase, Boolean>> removeLiveMutants(Map<Mutation, Map<TestCase, Boolean>> matrix) {
        return matrix.entrySet().stream()
            .filter(entry -> entry.getValue().containsValue(true))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Set<Mutation> selectDisjointMutants(Map<Mutation, Map<TestCase, Boolean>> matrix) {
        Set<Mutation> results = new HashSet<>();
        while (!matrix.isEmpty()) {
            Set<Mutation> subsumedMutants = new HashSet<>();
            Mutation disjointMutant = null;
            for (Map.Entry<Mutation, Map<TestCase, Boolean>> mutant : matrix.entrySet()) {
                Set<Mutation> currentSubsumedMutants = getSubsumedMutants(matrix, mutant.getValue());
                if (currentSubsumedMutants.size() > subsumedMutants.size()) {
                    disjointMutant = mutant.getKey();
                    subsumedMutants = currentSubsumedMutants;
                }
            }

            if (disjointMutant != null) {
                results.add(disjointMutant);
            }
            for (Mutation mutant : subsumedMutants) {
                matrix.remove(mutant);
            }
        }
        return results;
    }

    private Set<Mutation> getSubsumedMutants(Map<Mutation, Map<TestCase, Boolean>> matrix, Map<TestCase, Boolean> mutant) {
        return matrix.entrySet()
            .stream()
            .filter(entry -> compareMutants(mutant, entry.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    private boolean compareMutants(Map<TestCase, Boolean> mutant1, Map<TestCase, Boolean> mutant2) {
        return mutant1.entrySet().stream()
            .filter(Map.Entry::getValue)
            .allMatch(entry -> mutant2.getOrDefault(entry.getKey(), false));
    }

    private Set<DataSet> enrichDataSets(Set<DataSet> dataSets, Set<Mutation> disjointMutations) {
        return dataSets.stream()
            .map(dataSet -> dataSet.withMutations(dataSet.getMutations().stream()
                .map(mutation -> disjointMutations.contains(mutation) ? mutation.withDisjoint(true) : mutation)
                .collect(Collectors.toSet())))
            .collect(Collectors.toSet());
    }
}
