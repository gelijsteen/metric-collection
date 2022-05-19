package nl.uva.yamp.collector.callgraph.javassist;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.CallGraphConstructor;
import nl.uva.yamp.core.model.CallGraphMethod;
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
            .constructors(mapConstructors(root))
            .methods(mapMethods(root))
            .build();
    }

    private Set<CallGraphConstructor> mapConstructors(CallGraphNode root) {
        return root.getNodes().stream()
            .filter(this::isConstructor)
            .map(this::mapConstructorNode)
            .collect(Collectors.toSet());
    }

    private Set<CallGraphMethod> mapMethods(CallGraphNode root) {
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

    private CallGraphConstructor mapConstructorNode(CallGraphNode node) {
        return CallGraphConstructor.builder()
            .constructor(mapConstructor(node))
            .constructors(mapConstructors(node))
            .methods(mapMethods(node))
            .build();
    }

    private CallGraphMethod mapMethodNode(CallGraphNode node) {
        return CallGraphMethod.builder()
            .method(mapMethod(node))
            .constructors(mapConstructors(node))
            .methods(mapMethods(node))
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
