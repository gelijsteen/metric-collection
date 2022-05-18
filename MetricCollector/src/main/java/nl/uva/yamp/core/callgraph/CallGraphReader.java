package nl.uva.yamp.core.callgraph;

import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.Coverage;

public interface CallGraphReader {

    CallGraph read(Coverage coverage);
}
