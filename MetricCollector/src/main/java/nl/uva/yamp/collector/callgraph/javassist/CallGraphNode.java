package nl.uva.yamp.collector.callgraph.javassist;

import javassist.CtBehavior;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Builder
class CallGraphNode {

    private final CallGraphNode parent;
    @NonNull
    private final CtBehavior behavior;
    @NonNull
    private final Set<CallGraphNode> nodes = new HashSet<>();

    public String getFullyQualifiedMethodName() {
        return behavior.getDeclaringClass().getName() + "." + behavior.getName();
    }

    @Override
    public String toString() {
        return getFullyQualifiedMethodName() + " -> " + nodes;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        return getFullyQualifiedMethodName().equals(((CallGraphNode) that).getFullyQualifiedMethodName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFullyQualifiedMethodName());
    }
}
