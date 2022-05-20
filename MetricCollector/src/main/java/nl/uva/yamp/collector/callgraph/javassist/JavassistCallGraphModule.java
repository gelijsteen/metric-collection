package nl.uva.yamp.collector.callgraph.javassist;

import dagger.Binds;
import dagger.Module;
import nl.uva.yamp.core.collector.CallGraphCollector;

@Module
public interface JavassistCallGraphModule {

    @Binds
    CallGraphCollector callGraphCollector(JavassistCallGraphCollector impl);
}
