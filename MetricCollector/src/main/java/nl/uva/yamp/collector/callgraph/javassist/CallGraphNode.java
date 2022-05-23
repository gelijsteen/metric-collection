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

    String getSignature() {
        if (behavior.getMethodInfo().isConstructor()) {
            return behavior.getDeclaringClass().getName() + " " + behavior.getMethodInfo().getDescriptor();
        }

        if (behavior.getMethodInfo().isMethod()) {
            return behavior.getDeclaringClass().getName() + "." + behavior.getMethodInfo().getName() + " " + behavior.getMethodInfo().getDescriptor();
        }

        throw new IllegalStateException("Static initializers not supported.");
    }

    @Override
    public String toString() {
        return behavior.getDeclaringClass().getName() + "." + behavior.getName() + " -> " + nodes;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        return getSignature().equals(((CallGraphNode) that).getSignature());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSignature());
    }
}
