package nl.uva.yamp.collector.coverage.jacoco;

import dagger.Binds;
import dagger.Module;
import nl.uva.yamp.core.collector.CoverageCollector;

@Module
public interface JacocoCoverageModule {

    @Binds
    CoverageCollector coverageCollector(JacocoCoverageCollector impl);
}
