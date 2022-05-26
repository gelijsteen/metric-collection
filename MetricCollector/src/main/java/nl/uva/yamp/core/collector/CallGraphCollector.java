package nl.uva.yamp.core.collector;

import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.TargetDirectory;

public interface CallGraphCollector {

    DataSet collect(TargetDirectory targetDirectory, DataSet dataSet);
}
