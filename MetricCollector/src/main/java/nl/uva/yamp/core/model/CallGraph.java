package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Set;

@Getter

@Builder
@ToString
@EqualsAndHashCode
public class CallGraph {

    @NonNull
    private final Method testMethod;
    @NonNull
    private final Set<ConstructorNode> constructorNodes;
    @NonNull
    private final Set<MethodNode> methodNodes;

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class ConstructorNode {

        @NonNull
        private final Constructor constructor;
        @NonNull
        private final Set<ConstructorNode> constructorNodes;
        @NonNull
        private final Set<MethodNode> methodNodes;
    }

    @Getter
    @Builder
    @ToString
    @EqualsAndHashCode
    public static class MethodNode {

        @NonNull
        private final Method method;
        @NonNull
        private final Set<ConstructorNode> constructorNodes;
        @NonNull
        private final Set<MethodNode> methodNodes;
    }
}
