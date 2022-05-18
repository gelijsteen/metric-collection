package nl.uva.yamp.callgraph;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CallGraphConfiguration {

    @NonNull
    private NestedCallGraphConfiguration callGraph;

    @Getter
    @Setter
    public static class NestedCallGraphConfiguration {

        @NonNull
        private String projectDirectory;
    }
}
