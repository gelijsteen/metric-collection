package nl.uva.yamp.collector.mutation.pitest;

import dagger.Binds;
import dagger.Module;
import nl.uva.yamp.core.collector.MutationCollector;

@Module
public interface PitestMutationModule {

    @Binds
    MutationCollector mutationCollector(PitestMutationCollector impl);
}
