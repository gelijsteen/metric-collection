package nl.uva.meco.core;

import dagger.Module;
import dagger.Provides;
import nl.uva.meco.core.CoreConfiguration.DisjointMutantConfiguration;
import nl.uva.meco.core.CoreConfiguration.ParallelExecutionConfiguration;
import nl.uva.meco.core.metric.ApplicationLocMetricCollector;
import nl.uva.meco.core.metric.DeveloperClassificationMetricCollector;
import nl.uva.meco.core.metric.DisjointMutationScoreMetricCollector;
import nl.uva.meco.core.metric.DistinctPackageHierarchiesCoveredMetricCollector;
import nl.uva.meco.core.metric.IndirectClassesCoveredMetricCollector;
import nl.uva.meco.core.metric.IndirectMethodsCoveredMetricCollector;
import nl.uva.meco.core.metric.IndirectPackagesCoveredMetricCollector;
import nl.uva.meco.core.metric.KilledDisjointMutantMetricCollector;
import nl.uva.meco.core.metric.KilledMutantMetricCollector;
import nl.uva.meco.core.metric.MetricCollector;
import nl.uva.meco.core.metric.MutationScoreMetricCollector;
import nl.uva.meco.core.metric.RecursiveDirectnessMetricCalculator;
import nl.uva.meco.core.metric.RecursiveTdataMetricCollector;
import nl.uva.meco.core.metric.SurvivingDisjointMutantMetricCollector;
import nl.uva.meco.core.metric.SurvivingMutantMetricCollector;
import nl.uva.meco.core.metric.TestLocMetricCollector;
import nl.uva.meco.util.ConfigurationLoader;

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
                                                 TestLocMetricCollector testLocMetricCollector,
                                                 ApplicationLocMetricCollector applicationLocMetricCollector,
                                                 DeveloperClassificationMetricCollector developerClassificationMetricCollector,
                                                 KilledMutantMetricCollector killedMutantMetricCollector,
                                                 SurvivingMutantMetricCollector survivingMutantMetricCollector,
                                                 KilledDisjointMutantMetricCollector killedDisjointMutantMetricCollector,
                                                 SurvivingDisjointMutantMetricCollector survivingDisjointMutantMetricCollector,
                                                 MutationScoreMetricCollector mutationScoreMetricCollector,
                                                 DisjointMutationScoreMetricCollector disjointMutationScoreMetricCollector) {
        return List.of(
            recursiveTdataMetricCollector,
            indirectMethodsCoveredMetricCollector,
            indirectClassesCoveredMetricCollector,
            indirectPackagesCoveredMetricCollector,
            distinctPackageHierarchiesCoveredMetricCollector,
            recursiveDirectnessMetricCalculator,
            testLocMetricCollector,
            applicationLocMetricCollector,
            developerClassificationMetricCollector,
            killedMutantMetricCollector,
            survivingMutantMetricCollector,
            killedDisjointMutantMetricCollector,
            survivingDisjointMutantMetricCollector,
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
