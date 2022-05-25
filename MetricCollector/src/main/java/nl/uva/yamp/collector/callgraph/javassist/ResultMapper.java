package nl.uva.yamp.collector.callgraph.javassist;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.CallGraphConstructor;
import nl.uva.yamp.core.model.CallGraphMethod;
import nl.uva.yamp.core.model.TestCase;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
class ResultMapper {

    CallGraph map(TestCase testCase, Set<CallGraphNode> callGraphNodes) {
        Set<CallGraphNode> collect = callGraphNodes.stream()
            .flatMap(callGraphNode -> callGraphNode.getNodes().stream())
            .collect(Collectors.toSet());

        return CallGraph.builder()
            .testCase(testCase)
            .constructors(mapConstructors(collect))
            .methods(mapMethods(collect))
            .build();
    }

    private Set<CallGraphConstructor> mapConstructors(Set<CallGraphNode> root) {
        return root.stream()
            .filter(this::isConstructor)
            .map(this::mapConstructorNode)
            .collect(Collectors.toSet());
    }

    private Set<CallGraphMethod> mapMethods(Set<CallGraphNode> root) {
        return root.stream()
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
            .packageName(node.getBehavior().getDeclaringClass().getPackageName())
            .className(node.getBehavior().getDeclaringClass().getSimpleName())
            .descriptor(node.getBehavior().getMethodInfo().getDescriptor())
            .constructors(mapConstructors(node.getNodes()))
            .methods(mapMethods(node.getNodes()))
            .build();
    }

    private CallGraphMethod mapMethodNode(CallGraphNode node) {
        return CallGraphMethod.builder()
            .packageName(node.getBehavior().getDeclaringClass().getPackageName())
            .className(node.getBehavior().getDeclaringClass().getSimpleName())
            .methodName(node.getBehavior().getName())
            .descriptor(node.getBehavior().getMethodInfo().getDescriptor())
            .constructors(mapConstructors(node.getNodes()))
            .methods(mapMethods(node.getNodes()))
            .build();
    }
}
