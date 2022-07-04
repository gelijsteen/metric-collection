package nl.uva.meco.collector.coverage.jacoco;

import dagger.Binds;
import dagger.Module;
import nl.uva.meco.core.collector.CoverageCollector;

@Module
public interface JacocoCoverageModule {

    @Binds
    CoverageCollector coverageCollector(JacocoCoverageCollector impl);
}
