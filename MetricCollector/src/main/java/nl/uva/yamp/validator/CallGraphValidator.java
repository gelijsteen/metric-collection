package nl.uva.yamp.validator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.validator.Validator;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor(onConstructor = @__(@Inject))
public class CallGraphValidator implements Validator {

    @Override
    public void validate(Set<Coverage> coverages, Set<CallGraph> callGraphs) {
        coverages.forEach(coverage -> callGraphs.stream()
            .filter(callGraph -> coverage.getTestMethod().equals(callGraph.getTestMethod()))
            .findFirst()
            .map(callGraph -> Holder.builder()
                .testMethod(callGraph.getTestMethod())
                .coverage(coverage.getConstructors().size() + coverage.getMethods().size())
                .callGraph(countUniqueCallGraphNodes(callGraph))
                .build())
            .filter(holder -> holder.getCallGraph() != holder.getCoverage())
            .ifPresent(holder -> log.debug("Callgraph/coverage ratio for {}: {}/{}", holder.getTestMethod().getFullyQualifiedMethodName(), holder.getCallGraph(), holder.getCoverage())));
    }

    private int countUniqueCallGraphNodes(CallGraph callGraph) {
        int count1 = (int) Stream.concat(
            callGraph.getConstructorNodes().stream().flatMap(this::collectConstructors),
            callGraph.getMethodNodes().stream().flatMap(this::collectConstructors)
        ).distinct().count();
        int count2 = (int) Stream.concat(
            callGraph.getConstructorNodes().stream().flatMap(this::collectMethods),
            callGraph.getMethodNodes().stream().flatMap(this::collectMethods)
        ).distinct().count();

        return count1 + count2;
    }

    private Stream<Constructor> collectConstructors(CallGraph.ConstructorNode constructorNode) {
        return Stream.concat(
            Stream.of(constructorNode.getConstructor()),
            Stream.concat(
                constructorNode.getConstructorNodes().stream().flatMap(this::collectConstructors),
                constructorNode.getMethodNodes().stream().flatMap(this::collectConstructors)
            )
        );
    }

    private Stream<Constructor> collectConstructors(CallGraph.MethodNode methodNode) {
        return Stream.concat(
            methodNode.getConstructorNodes().stream().flatMap(this::collectConstructors),
            methodNode.getMethodNodes().stream().flatMap(this::collectConstructors)
        );
    }

    private Stream<Method> collectMethods(CallGraph.ConstructorNode constructorNode) {
        return Stream.concat(
            constructorNode.getConstructorNodes().stream().flatMap(this::collectMethods),
            constructorNode.getMethodNodes().stream().flatMap(this::collectMethods)
        );
    }

    private Stream<Method> collectMethods(CallGraph.MethodNode methodNode) {
        return Stream.concat(
            Stream.of(methodNode.getMethod()),
            Stream.concat(
                methodNode.getConstructorNodes().stream().flatMap(this::collectMethods),
                methodNode.getMethodNodes().stream().flatMap(this::collectMethods)
            )
        );
    }

    @Getter
    @Builder
    private static class Holder {

        @NonNull
        private final Method testMethod;
        private final int coverage;
        private final int callGraph;
    }
}
