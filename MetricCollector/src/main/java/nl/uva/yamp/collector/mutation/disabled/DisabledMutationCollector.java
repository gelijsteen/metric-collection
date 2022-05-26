package nl.uva.yamp.collector.mutation.disabled;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.TargetDirectory;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DisabledMutationCollector implements MutationCollector {

    @Override
    public DataSet collect(TargetDirectory targetDirectory, DataSet dataSet) {
        return dataSet.withMutationScore(0);
    }
}
