package nl.uva.yamp.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.CallGraphConstructor;
import nl.uva.yamp.core.model.CallGraphMethod;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.TestCase;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.TestMetrics;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoreTestData {

    public static final int MUTATION_SCORE = 100;
    public static final String PACKAGE_NAME = "a.b.c";
    public static final String CLASS_NAME = "D";
    public static final String METHOD_NAME = "e";
    public static final String METRIC_IDENTIFIER = "identifier";
    public static final int METRIC_VALUE = 0;

    public static Coverage.CoverageBuilder coverageBuilder() {
        return Coverage.builder()
            .testCase(testCaseBuilder().build())
            .constructors(Set.of(constructorBuilder().build()))
            .methods(Set.of(methodBuilder().build()));
    }

    public static CallGraph.CallGraphBuilder callGraphBuilder() {
        return CallGraph.builder()
            .testCase(testCaseBuilder().build())
            .constructors(Set.of(callGraphConstructorBuilder().build()))
            .methods(Set.of(callGraphMethodBuilder().build()));
    }

    public static CallGraphConstructor.CallGraphConstructorBuilder callGraphConstructorBuilder() {
        return CallGraphConstructor.builder()
            .constructor(constructorBuilder().build())
            .constructors(Set.of())
            .methods(Set.of());
    }

    public static CallGraphMethod.CallGraphMethodBuilder callGraphMethodBuilder() {
        return CallGraphMethod.builder()
            .method(methodBuilder().build())
            .constructors(Set.of())
            .methods(Set.of());
    }

    public static Mutation.MutationBuilder mutationBuilder() {
        return Mutation.builder()
            .testCase(testCaseBuilder().build())
            .mutationScore(MUTATION_SCORE);
    }

    public static CombinedData.CombinedDataBuilder combinedDataBuilder() {
        return CombinedData.builder()
            .testCase(testCaseBuilder().build())
            .constructors(Set.of(constructorBuilder().build()))
            .methods(Set.of(methodBuilder().build()))
            .mutationScore(MUTATION_SCORE);
    }

    public static TestCase.TestCaseBuilder testCaseBuilder() {
        return TestCase.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME);
    }

    public static Constructor.ConstructorBuilder constructorBuilder() {
        return Constructor.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME);
    }

    public static Method.MethodBuilder methodBuilder() {
        return Method.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME);
    }

    public static TestMetrics.TestMetricsBuilder testMetricsBuilder() {
        return TestMetrics.builder()
            .testCase(testCaseBuilder().build())
            .metrics(List.of(integerMetricBuilder().build()));
    }

    public static IntegerMetric.IntegerMetricBuilder integerMetricBuilder() {
        return IntegerMetric.builder()
            .identifier(METRIC_IDENTIFIER)
            .value(METRIC_VALUE);
    }
}
