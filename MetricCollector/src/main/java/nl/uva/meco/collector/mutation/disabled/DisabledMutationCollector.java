package nl.uva.meco.collector.mutation.disabled;

import lombok.NoArgsConstructor;
import nl.uva.meco.core.collector.MutationCollector;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.TargetDirectory;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DisabledMutationCollector implements MutationCollector {

    @Override
    public DataSet collect(TargetDirectory targetDirectory, DataSet dataSet) {
        return dataSet;
    }
}
