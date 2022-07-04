package nl.uva.meco.core.enricher;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.meco.core.CoreConfiguration.DisjointMutantConfiguration;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.Mutation;
import nl.uva.meco.core.model.TestCase;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DisjointMutantEnricher {

    private final ForkJoinPool forkJoinPool;

    private final DisjointMutantConfiguration configuration;

    @SneakyThrows
    public Set<DataSet> enrich(Set<DataSet> dataSets) {
        Set<Mutation> collect = forkJoinPool.submit(() -> IntStream.range(0, configuration.getRepetitions())
            .parallel()
            .peek(i -> log.info("Calculating disjoint mutation {}/{}", i + 1, configuration.getRepetitions()))
            .mapToObj(i -> applyDisjointMutantAlgorithm(dataSets))
            .flatMap(Collection::stream)
            .collect(Collectors.toSet())).get();
        return enrichDataSets(dataSets, collect);
    }

    private Set<Mutation> applyDisjointMutantAlgorithm(Set<DataSet> dataSets) {
        Map<Mutation, Map<TestCase, Boolean>> matrix = mapToMatrix(dataSets);
        Map<Mutation, Map<TestCase, Boolean>> filteredMatrix = removeLiveMutants(matrix);
        return selectDisjointMutants(filteredMatrix);
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
            List<Map.Entry<Mutation, Map<TestCase, Boolean>>> list = shuffleMatrix(matrix);
            for (Map.Entry<Mutation, Map<TestCase, Boolean>> mutant : list) {
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

    private List<Map.Entry<Mutation, Map<TestCase, Boolean>>> shuffleMatrix(Map<Mutation, Map<TestCase, Boolean>> matrix) {
        List<Map.Entry<Mutation, Map<TestCase, Boolean>>> list = new ArrayList<>(matrix.entrySet());
        Collections.shuffle(list);
        return list;
    }

    private Set<Mutation> getSubsumedMutants(Map<Mutation, Map<TestCase, Boolean>> matrix, Map<TestCase, Boolean> mutant) {
        return matrix.entrySet()
            .stream()
            .filter(entry -> subsumesMutant(mutant, entry.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    private boolean subsumesMutant(Map<TestCase, Boolean> mutant1, Map<TestCase, Boolean> mutant2) {
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
