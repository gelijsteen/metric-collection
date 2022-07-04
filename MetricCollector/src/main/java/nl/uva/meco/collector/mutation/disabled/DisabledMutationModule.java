package nl.uva.meco.collector.mutation.disabled;

import dagger.Binds;
import dagger.Module;
import nl.uva.meco.core.collector.MutationCollector;

@Module
public interface DisabledMutationModule {

    @Binds
    MutationCollector mutationCollector(DisabledMutationCollector impl);
}
