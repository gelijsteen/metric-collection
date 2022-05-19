package nl.uva.yamp.callgraph.disabled;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.callgraph.CallGraphReader;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.Coverage;

import javax.inject.Inject;
import java.util.Set;

@NoArgsConstructor(onConstructor = @__(@Inject))
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
