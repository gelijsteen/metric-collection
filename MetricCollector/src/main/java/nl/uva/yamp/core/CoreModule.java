package nl.uva.yamp.core;

import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.core.metric.DistinctPackageHierarchiesCoveredMetricCollector;
import nl.uva.yamp.core.metric.IndirectClassesCoveredMetricCollector;
import nl.uva.yamp.core.metric.IndirectMethodsCoveredMetricCollector;
import nl.uva.yamp.core.metric.IndirectPackagesCoveredMetricCollector;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.metric.MutationScoreMetricCollector;
import nl.uva.yamp.core.metric.RecursiveDirectnessMetricCalculator;
import nl.uva.yamp.core.metric.RecursiveTdataMetricCollector;

import java.util.List;

@Module
public interface CoreModule {

    @Provides
    static List<MetricCollector> metricCollector(RecursiveTdataMetricCollector recursiveTdataMetricCollector,
                                                 IndirectMethodsCoveredMetricCollector indirectMethodsCoveredMetricCollector,
                                                 IndirectClassesCoveredMetricCollector indirectClassesCoveredMetricCollector,
                                                 IndirectPackagesCoveredMetricCollector indirectPackagesCoveredMetricCollector,
                                                 DistinctPackageHierarchiesCoveredMetricCollector distinctPackageHierarchiesCoveredMetricCollector,
                                                 RecursiveDirectnessMetricCalculator recursiveDirectnessMetricCalculator,
                                                 MutationScoreMetricCollector mutationScoreMetricCollector) {
        return List.of(
            recursiveTdataMetricCollector,
            indirectMethodsCoveredMetricCollector,
            indirectClassesCoveredMetricCollector,
            indirectPackagesCoveredMetricCollector,
            distinctPackageHierarchiesCoveredMetricCollector,
            recursiveDirectnessMetricCalculator,
            mutationScoreMetricCollector);
    }
}
