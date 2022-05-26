package nl.uva.yamp.core.collector;

import nl.uva.yamp.core.model.TargetDirectory;

import java.util.Set;

public interface TargetCollector {

    Set<TargetDirectory> collect();
}
