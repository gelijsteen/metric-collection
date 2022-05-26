package nl.uva.yamp.core.collector;

import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.TargetDirectory;

import java.util.Set;

public interface CoverageCollector {

    Set<DataSet> collect(TargetDirectory targetDirectory);
}
