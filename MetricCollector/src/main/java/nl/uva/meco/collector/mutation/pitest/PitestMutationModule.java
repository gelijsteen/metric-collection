package nl.uva.meco.collector.mutation.pitest;

import dagger.Binds;
import dagger.Module;
import nl.uva.meco.core.collector.MutationCollector;

@Module
public interface PitestMutationModule {

    @Binds
    MutationCollector mutationCollector(PitestMutationCollector impl);
}
