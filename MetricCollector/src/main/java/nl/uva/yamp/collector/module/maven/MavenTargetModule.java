package nl.uva.yamp.collector.module.maven;

import dagger.Binds;
import dagger.Module;
import nl.uva.yamp.core.collector.TargetCollector;

@Module
public interface MavenTargetModule {

    @Binds
    TargetCollector targetCollector(MavenTargetCollector impl);
}
