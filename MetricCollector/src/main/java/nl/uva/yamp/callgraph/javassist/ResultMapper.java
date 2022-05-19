package nl.uva.yamp.callgraph.javassist;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.Method;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class ResultMapper {

    CallGraph map(CallGraphNode root) {
        return CallGraph.builder()
            .testMethod(mapMethod(root))
            .constructorNodes(mapConstructors(root))
            .methodNodes(mapMethods(root))
            .build();
    }

    private Set<CallGraph.ConstructorNode> mapConstructors(CallGraphNode root) {
        return root.getNodes().stream()
            .filter(this::isConstructor)
            .map(this::mapConstructorNode)
            .collect(Collectors.toSet());
    }

    private Set<CallGraph.MethodNode> mapMethods(CallGraphNode root) {
        return root.getNodes().stream()
            .filter(this::isMethod)
            .map(this::mapMethodNode)
            .collect(Collectors.toSet());
    }

    private boolean isConstructor(CallGraphNode node) {
        return node.getBehavior().getMethodInfo().isConstructor();
    }

    private boolean isMethod(CallGraphNode node) {
        return node.getBehavior().getMethodInfo().isMethod();
    }

    private CallGraph.ConstructorNode mapConstructorNode(CallGraphNode node) {
        return CallGraph.ConstructorNode.builder()
            .constructor(mapConstructor(node))
            .constructorNodes(mapConstructors(node))
            .methodNodes(mapMethods(node))
            .build();
    }

    private CallGraph.MethodNode mapMethodNode(CallGraphNode node) {
        return CallGraph.MethodNode.builder()
            .method(mapMethod(node))
            .constructorNodes(mapConstructors(node))
            .methodNodes(mapMethods(node))
            .build();
    }

    private Constructor mapConstructor(CallGraphNode node) {
        return Constructor.builder()
            .packageName(node.getBehavior().getDeclaringClass().getPackageName())
            .className(node.getBehavior().getDeclaringClass().getSimpleName())
            .build();
    }

    private Method mapMethod(CallGraphNode node) {
        return Method.builder()
            .packageName(node.getBehavior().getDeclaringClass().getPackageName())
            .className(node.getBehavior().getDeclaringClass().getSimpleName())
            .methodName(node.getBehavior().getName())
            .build();
    }
}
