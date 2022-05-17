package nl.uva.yamp.core.coverage;

import nl.uva.yamp.core.model.Coverage;

import java.util.Set;

public interface CoverageReader {

    Set<Coverage> read();
}
