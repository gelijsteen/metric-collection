package nl.uva.yamp.core.collector;

import nl.uva.yamp.core.model.Coverage;

import java.util.Set;

public interface CoverageCollector {

    Set<Coverage> collect();
}
