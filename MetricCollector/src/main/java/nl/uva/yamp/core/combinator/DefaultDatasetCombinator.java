package nl.uva.yamp.core.combinator;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.CallGraphConstructor;
import nl.uva.yamp.core.model.CallGraphMethod;
import nl.uva.yamp.core.model.CombinedConstructor;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.CombinedMethod;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.CoverageConstructor;
import nl.uva.yamp.core.model.CoverageMethod;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.TestCase;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DefaultDatasetCombinator implements DatasetCombinator {

    @Override
    public Set<CombinedData> combine(Set<Coverage> coverageData, Set<CallGraph> callGraphData, Set<Mutation> mutationData) {
        Map<TestCase, Coverage> coverageMap = coverageData.stream()
            .collect(Collectors.toMap(Coverage::getTestCase, coverage -> coverage));
        Map<TestCase, Mutation> mutationMap = mutationData.stream()
            .collect(Collectors.toMap(Mutation::getTestCase, mutation -> mutation));

        return callGraphData.stream()
            .map(callGraph -> {
                Coverage coverage = coverageMap.get(callGraph.getTestCase());
                Mutation mutation = mutationMap.get(callGraph.getTestCase());
                return combine(coverage, callGraph, mutation);
            })
            .collect(Collectors.toSet());
    }

    private CombinedData combine(Coverage coverage, CallGraph callGraph, Mutation mutation) {
        Map<String, CoverageConstructor> constructors = coverage.getConstructors().stream()
            .collect(Collectors.toMap(CoverageConstructor::getFullyQualifiedClassName, constructor -> constructor));
        Map<String, CoverageMethod> methods = coverage.getMethods().stream()
            .collect(Collectors.toMap(CoverageMethod::getFullyQualifiedMethodName, method -> method));

        return CombinedData.builder()
            .testCase(callGraph.getTestCase())
            .constructors(getConstructors(callGraph.getConstructors(), constructors, methods))
            .methods(getMethods(callGraph.getMethods(), constructors, methods))
            .mutationScore(mutation.getMutationScore())
            .build();
    }

    private Set<CombinedConstructor> getConstructors(Set<CallGraphConstructor> constructors,
                                                     Map<String, CoverageConstructor> constructorMap,
                                                     Map<String, CoverageMethod> methodMap) {
        return constructors.stream()
            .map(callGraphConstructor -> getConstructor(callGraphConstructor, constructorMap, methodMap))
            .collect(Collectors.toSet());
    }

    private Set<CombinedMethod> getMethods(Set<CallGraphMethod> methods,
                                           Map<String, CoverageConstructor> constructorMap,
                                           Map<String, CoverageMethod> methodMap) {
        return methods.stream()
            .map(callGraphMethod -> getMethod(callGraphMethod, constructorMap, methodMap))
            .collect(Collectors.toSet());
    }

    private CombinedConstructor getConstructor(CallGraphConstructor callGraphConstructor,
                                               Map<String, CoverageConstructor> constructorMap,
                                               Map<String, CoverageMethod> methodMap) {
        return CombinedConstructor.builder()
            .packageName(callGraphConstructor.getPackageName())
            .className(callGraphConstructor.getClassName())
            .constructors(getConstructors(callGraphConstructor.getConstructors(), constructorMap, methodMap))
            .methods(getMethods(callGraphConstructor.getMethods(), constructorMap, methodMap))
            .loc(constructorMap.get(callGraphConstructor.getFullyQualifiedClassName()).getLoc())
            .build();
    }

    private CombinedMethod getMethod(CallGraphMethod callGraphMethod,
                                     Map<String, CoverageConstructor> constructorMap,
                                     Map<String, CoverageMethod> methodMap) {
        return CombinedMethod.builder()
            .packageName(callGraphMethod.getPackageName())
            .className(callGraphMethod.getClassName())
            .methodName(callGraphMethod.getMethodName())
            .constructors(getConstructors(callGraphMethod.getConstructors(), constructorMap, methodMap))
            .methods(getMethods(callGraphMethod.getMethods(), constructorMap, methodMap))
            .loc(methodMap.get(callGraphMethod.getFullyQualifiedMethodName()).getLoc())
            .build();
    }
}
