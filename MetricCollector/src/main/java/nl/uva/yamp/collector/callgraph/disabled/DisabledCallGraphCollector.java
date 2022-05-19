package nl.uva.yamp.collector.callgraph.disabled;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.collector.CallGraphCollector;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.Coverage;

import javax.inject.Inject;
import java.util.Set;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DisabledCallGraphCollector implements CallGraphCollector {

    @Override
    public CallGraph collect(Coverage coverage) {
        return CallGraph.builder()
            .testCase(coverage.getTestCase())
            .constructors(Set.of())
            .methods(Set.of())
            .build();
    }
}
