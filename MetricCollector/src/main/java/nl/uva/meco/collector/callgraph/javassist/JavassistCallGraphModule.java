package nl.uva.meco.collector.callgraph.javassist;

import dagger.Binds;
import dagger.Module;
import nl.uva.meco.core.collector.CallGraphCollector;

@Module
public interface JavassistCallGraphModule {

    @Binds
    CallGraphCollector callGraphCollector(JavassistCallGraphCollector impl);
}
