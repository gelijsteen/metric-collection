package nl.uva.yamp.core.collector;

import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.Coverage;

public interface CallGraphCollector {

    CallGraph collect(Coverage coverage);
}
