package nl.uva.meco.collector.module.maven;

import dagger.Binds;
import dagger.Module;
import nl.uva.meco.core.collector.TargetCollector;

@Module
public interface MavenTargetModule {

    @Binds
    TargetCollector targetCollector(MavenTargetCollector impl);
}
