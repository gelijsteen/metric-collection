package nl.uva.yamp.callgraph.disabled;

import nl.uva.yamp.core.callgraph.CallGraphReader;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.Coverage;

import java.util.Set;

public class DisabledCallGraphReader implements CallGraphReader {

    @Override
    public CallGraph read(Coverage coverage) {
        return CallGraph.builder()
            .testMethod(coverage.getTestMethod())
            .constructorNodes(Set.of())
            .methodNodes(Set.of())
            .build();
    }
}
