package nl.uva.yamp.collector.callgraph.javassist;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.CallGraphConstructor;
import nl.uva.yamp.core.model.CallGraphMethod;
import nl.uva.yamp.core.model.TestCase;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(onConstructor = @__(@Inject))
class ResultMapper {

    CallGraph map(TestCase testCase, CallGraphNode constructorRoot, CallGraphNode methodRoot) {
        return CallGraph.builder()
            .testCase(testCase)
            .constructors(mapConstructors(Stream.concat(constructorRoot.getNodes().stream(), methodRoot.getNodes().stream())))
            .methods(mapMethods(Stream.concat(constructorRoot.getNodes().stream(), methodRoot.getNodes().stream())))
            .build();
    }

    private Set<CallGraphConstructor> mapConstructors(Stream<CallGraphNode> root) {
        return root
            .filter(this::isConstructor)
            .map(this::mapConstructorNode)
            .collect(Collectors.toSet());
    }

    private Set<CallGraphMethod> mapMethods(Stream<CallGraphNode> root) {
        return root
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
            .constructors(mapConstructors(node.getNodes().stream()))
            .methods(mapMethods(node.getNodes().stream()))
            .build();
    }

    private CallGraphMethod mapMethodNode(CallGraphNode node) {
        return CallGraphMethod.builder()
            .packageName(node.getBehavior().getDeclaringClass().getPackageName())
            .className(node.getBehavior().getDeclaringClass().getSimpleName())
            .methodName(node.getBehavior().getName())
            .descriptor(node.getBehavior().getMethodInfo().getDescriptor())
            .constructors(mapConstructors(node.getNodes().stream()))
            .methods(mapMethods(node.getNodes().stream()))
            .build();
    }
}
