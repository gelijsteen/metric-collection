package nl.uva.meco.core.collector;

import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.TargetDirectory;

public interface CallGraphCollector {

    DataSet collect(TargetDirectory targetDirectory, DataSet dataSet);
}
