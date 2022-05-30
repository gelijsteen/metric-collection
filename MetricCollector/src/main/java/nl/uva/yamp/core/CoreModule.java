package nl.uva.yamp.core;

import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.core.CoreConfiguration.DisjointMutantConfiguration;
import nl.uva.yamp.core.CoreConfiguration.ParallelExecutionConfiguration;
import nl.uva.yamp.core.metric.DisjointMutationScoreMetricCollector;
import nl.uva.yamp.core.metric.DistinctPackageHierarchiesCoveredMetricCollector;
import nl.uva.yamp.core.metric.IndirectClassesCoveredMetricCollector;
import nl.uva.yamp.core.metric.IndirectMethodsCoveredMetricCollector;
import nl.uva.yamp.core.metric.IndirectPackagesCoveredMetricCollector;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.metric.MutationScoreMetricCollector;
import nl.uva.yamp.core.metric.RecursiveDirectnessMetricCalculator;
import nl.uva.yamp.core.metric.RecursiveTdataMetricCollector;
import nl.uva.yamp.util.ConfigurationLoader;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

@Module
public interface CoreModule {

    @Provides
    static List<MetricCollector> metricCollector(RecursiveTdataMetricCollector recursiveTdataMetricCollector,
                                                 IndirectMethodsCoveredMetricCollector indirectMethodsCoveredMetricCollector,
                                                 IndirectClassesCoveredMetricCollector indirectClassesCoveredMetricCollector,
                                                 IndirectPackagesCoveredMetricCollector indirectPackagesCoveredMetricCollector,
                                                 DistinctPackageHierarchiesCoveredMetricCollector distinctPackageHierarchiesCoveredMetricCollector,
                                                 RecursiveDirectnessMetricCalculator recursiveDirectnessMetricCalculator,
                                                 MutationScoreMetricCollector mutationScoreMetricCollector,
                                                 DisjointMutationScoreMetricCollector disjointMutationScoreMetricCollector) {
        return List.of(
            recursiveTdataMetricCollector,
            indirectMethodsCoveredMetricCollector,
            indirectClassesCoveredMetricCollector,
            indirectPackagesCoveredMetricCollector,
            distinctPackageHierarchiesCoveredMetricCollector,
            recursiveDirectnessMetricCalculator,
            mutationScoreMetricCollector,
            disjointMutationScoreMetricCollector);
    }

    @Provides
    static ParallelExecutionConfiguration parallelExecutionConfiguration() {
        CoreConfiguration coreConfiguration = ConfigurationLoader.loadConfiguration(CoreConfiguration.class);
        return Optional.ofNullable(coreConfiguration)
            .map(CoreConfiguration::getParallelExecution)
            .orElseThrow();
    }

    @Provides
    static DisjointMutantConfiguration disjointMutantConfiguration() {
        CoreConfiguration coreConfiguration = ConfigurationLoader.loadConfiguration(CoreConfiguration.class);
        return Optional.ofNullable(coreConfiguration)
            .map(CoreConfiguration::getDisjointMutants)
            .orElseThrow();
    }

    @Provides
    static ForkJoinPool forkJoinPool(ParallelExecutionConfiguration configuration) {
        return new ForkJoinPool(configuration.getNumThreads());
    }
}
