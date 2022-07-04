package nl.uva.meco.core.collector;

import nl.uva.meco.core.model.TargetDirectory;

import java.util.Set;

public interface TargetCollector {

    Set<TargetDirectory> collect();
}
