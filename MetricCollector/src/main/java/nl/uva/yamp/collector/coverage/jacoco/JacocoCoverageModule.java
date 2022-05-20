package nl.uva.yamp.collector.coverage.jacoco;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.collector.coverage.CoverageConfiguration;
import nl.uva.yamp.core.collector.CoverageCollector;
import nl.uva.yamp.util.ConfigurationLoader;

import java.util.Optional;

@Module
public interface JacocoCoverageModule {

    @Binds
    CoverageCollector coverageCollector(JacocoCoverageCollector impl);

    @Provides
    static JacocoCoverageConfiguration jacocoCoverageConfiguration() {
        CoverageConfiguration coverageConfiguration = ConfigurationLoader.loadConfiguration(CoverageConfiguration.class);
        return Optional.ofNullable(coverageConfiguration)
            .map(CoverageConfiguration::getCoverage)
            .map(CoverageConfiguration.NestedCoverageConfiguration::getJacoco)
            .orElseThrow();
    }
}
