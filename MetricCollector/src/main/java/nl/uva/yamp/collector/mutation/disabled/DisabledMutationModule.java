package nl.uva.yamp.collector.mutation.disabled;

import dagger.Binds;
import dagger.Module;
import nl.uva.yamp.core.collector.MutationCollector;

@Module
public interface DisabledMutationModule {

    @Binds
    MutationCollector mutationCollector(DisabledMutationCollector impl);
}
