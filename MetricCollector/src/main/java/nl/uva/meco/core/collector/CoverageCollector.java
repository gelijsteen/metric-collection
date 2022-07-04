package nl.uva.meco.core.collector;

import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.TargetDirectory;

import java.util.Set;

public interface CoverageCollector {

    Set<DataSet> collect(TargetDirectory targetDirectory);
}
