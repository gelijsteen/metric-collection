package nl.uva.yamp.core.validator;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.CallGraphConstructor;
import nl.uva.yamp.core.model.CallGraphMethod;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.TestCase;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor(onConstructor = @__(@Inject))
public class CallGraphValidator implements Validator {

    @Override
    public void validate(Set<Coverage> coverages, Set<CallGraph> callGraphs) {
        coverages.forEach(coverage -> callGraphs.stream()
            .filter(callGraph -> coverage.getTestCase().equals(callGraph.getTestCase()))
            .findFirst()
            .map(callGraph -> Holder.builder()
                .testCase(callGraph.getTestCase())
                .coverage(coverage.getConstructors().size() + coverage.getMethods().size())
                .callGraph(countUniqueCallGraphNodes(callGraph))
                .build())
            .filter(holder -> holder.getCallGraph() != holder.getCoverage())
            .ifPresent(holder -> log.debug("Callgraph/coverage ratio for {}: {}/{}", holder.getTestCase().getFullyQualifiedMethodName(), holder.getCallGraph(), holder.getCoverage())));
    }

    private int countUniqueCallGraphNodes(CallGraph callGraph) {
        int distinctConstructors = (int) Stream.concat(
                callGraph.getConstructors().stream().flatMap(this::collectConstructors),
                callGraph.getMethods().stream().flatMap(this::collectConstructors)
            )
            .map(CallGraphConstructor::getSignature)
            .distinct()
            .count();
        int distinctMethods = (int) Stream.concat(
                callGraph.getConstructors().stream().flatMap(this::collectMethods),
                callGraph.getMethods().stream().flatMap(this::collectMethods)
            )
            .map(CallGraphMethod::getSignature)
            .distinct()
            .count();

        return distinctConstructors + distinctMethods;
    }

    private Stream<CallGraphConstructor> collectConstructors(CallGraphConstructor constructorNode) {
        return Stream.concat(
            Stream.of(constructorNode),
            Stream.concat(
                constructorNode.getConstructors().stream().flatMap(this::collectConstructors),
                constructorNode.getMethods().stream().flatMap(this::collectConstructors)
            )
        );
    }

    private Stream<CallGraphConstructor> collectConstructors(CallGraphMethod methodNode) {
        return Stream.concat(
            methodNode.getConstructors().stream().flatMap(this::collectConstructors),
            methodNode.getMethods().stream().flatMap(this::collectConstructors)
        );
    }

    private Stream<CallGraphMethod> collectMethods(CallGraphConstructor constructorNode) {
        return Stream.concat(
            constructorNode.getConstructors().stream().flatMap(this::collectMethods),
            constructorNode.getMethods().stream().flatMap(this::collectMethods)
        );
    }

    private Stream<CallGraphMethod> collectMethods(CallGraphMethod methodNode) {
        return Stream.concat(
            Stream.of(methodNode),
            Stream.concat(
                methodNode.getConstructors().stream().flatMap(this::collectMethods),
                methodNode.getMethods().stream().flatMap(this::collectMethods)
            )
        );
    }

    @Getter
    @Builder
    private static class Holder {

        @NonNull
        private final TestCase testCase;
        private final int coverage;
        private final int callGraph;
    }
}
