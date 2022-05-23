package nl.uva.yamp.core;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.core.combinator.DatasetCombinator;
import nl.uva.yamp.core.combinator.DefaultDatasetCombinator;
import nl.uva.yamp.core.filter.Filter;
import nl.uva.yamp.core.metric.IndirectClassesCoveredMetricCollector;
import nl.uva.yamp.core.metric.IndirectMethodsCoveredMetricCollector;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.metric.MutationScoreMetricCollector;
import nl.uva.yamp.core.metric.IndirectPackagesCoveredMetricCollector;
import nl.uva.yamp.core.validator.CallGraphValidator;
import nl.uva.yamp.core.validator.Validator;

import java.util.List;

@Module
public interface CoreModule {

    @Binds
    Validator defaultValidator(CallGraphValidator impl);

    @Binds
    DatasetCombinator defaultDatasetCombinator(DefaultDatasetCombinator impl);

    @Provides
    static List<MetricCollector> metricCollector(IndirectPackagesCoveredMetricCollector indirectPackagesCoveredMetricCollector,
                                                 IndirectClassesCoveredMetricCollector indirectClassesCoveredMetricCollector,
                                                 IndirectMethodsCoveredMetricCollector indirectMethodsCoveredMetricCollector,
                                                 MutationScoreMetricCollector mutationScoreMetricCollector) {
        return List.of(
            indirectPackagesCoveredMetricCollector,
            indirectClassesCoveredMetricCollector,
            indirectMethodsCoveredMetricCollector,
            mutationScoreMetricCollector);
    }

    @Provides
    static List<Filter> filters() {
        return List.of();
    }
}
