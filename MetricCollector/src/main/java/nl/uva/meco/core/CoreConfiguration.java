package nl.uva.meco.core;

import lombok.Setter;

import java.util.Optional;

@Setter
public class CoreConfiguration {

    private ParallelExecutionConfiguration parallelExecution;
    private DisjointMutantConfiguration disjointMutants;

    public ParallelExecutionConfiguration getParallelExecution() {
        return Optional.ofNullable(parallelExecution).orElse(new ParallelExecutionConfiguration());
    }

    public DisjointMutantConfiguration getDisjointMutants() {
        return Optional.ofNullable(disjointMutants).orElse(new DisjointMutantConfiguration());
    }

    @Setter
    public static class ParallelExecutionConfiguration {

        private Integer numThreads;

        public Integer getNumThreads() {
            return Optional.ofNullable(numThreads).orElse(1);
        }
    }

    @Setter
    public static class DisjointMutantConfiguration {

        private Integer repetitions;

        public Integer getRepetitions() {
            return Optional.ofNullable(repetitions).orElse(8);
        }
    }
}
