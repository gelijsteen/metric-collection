package nl.uva.yamp.core;

import lombok.AccessLevel;
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
import nl.uva.yamp.core.model.metric.DoubleMetric;
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
    public static final String DESCRIPTOR = "()V";
    public static final String METRIC_IDENTIFIER = "identifier";
    public static final int METRIC_VALUE_INTEGER = 0;
    public static final int COVERAGE_LOC = 0;
    private static final double METRIC_VALUE_DOUBLE = 0d;

    public static TestCase.TestCaseBuilder testCaseBuilder() {
        return TestCase.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME);
    }

    public static Coverage.CoverageBuilder coverageBuilder() {
        return Coverage.builder()
            .testCase(testCaseBuilder().build())
            .constructors(Set.of(coverageConstructorBuilder().build()))
            .methods(Set.of(coverageMethodBuilder().build()))
            .testConstructors(Set.of(coverageConstructorBuilder().build()))
            .testMethods(Set.of(coverageMethodBuilder().build()));
    }

    public static CoverageConstructor.CoverageConstructorBuilder coverageConstructorBuilder() {
        return CoverageConstructor.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .descriptor(DESCRIPTOR)
            .loc(COVERAGE_LOC);
    }

    public static CoverageMethod.CoverageMethodBuilder coverageMethodBuilder() {
        return CoverageMethod.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME)
            .descriptor(DESCRIPTOR)
            .loc(COVERAGE_LOC);
    }

    public static CallGraph.CallGraphBuilder callGraphBuilder() {
        return CallGraph.builder()
            .testCase(testCaseBuilder().build())
            .constructors(Set.of(callGraphConstructorBuilder().build()))
            .methods(Set.of(callGraphMethodBuilder().build()));
    }

    public static CallGraphConstructor.CallGraphConstructorBuilder callGraphConstructorBuilder() {
        return CallGraphConstructor.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .descriptor(DESCRIPTOR)
            .constructors(Set.of())
            .methods(Set.of());
    }

    public static CallGraphMethod.CallGraphMethodBuilder callGraphMethodBuilder() {
        return CallGraphMethod.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME)
            .descriptor(DESCRIPTOR)
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
            .constructors(Set.of(combinedConstructorBuilder().build()))
            .methods(Set.of(combinedMethodBuilder().build()))
            .mutationScore(MUTATION_SCORE);
    }

    public static CombinedConstructor.CombinedConstructorBuilder combinedConstructorBuilder() {
        return CombinedConstructor.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .descriptor(DESCRIPTOR)
            .constructors(Set.of())
            .methods(Set.of())
            .loc(COVERAGE_LOC);
    }

    public static CombinedMethod.CombinedMethodBuilder combinedMethodBuilder() {
        return CombinedMethod.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME)
            .descriptor(DESCRIPTOR)
            .constructors(Set.of())
            .methods(Set.of())
            .loc(COVERAGE_LOC);
    }

    public static TestMetrics.TestMetricsBuilder testMetricsBuilder() {
        return TestMetrics.builder()
            .testCase(testCaseBuilder().build())
            .metrics(List.of(integerMetricBuilder().build()));
    }

    public static IntegerMetric.IntegerMetricBuilder integerMetricBuilder() {
        return IntegerMetric.builder()
            .identifier(METRIC_IDENTIFIER)
            .value(METRIC_VALUE_INTEGER);
    }

    public static DoubleMetric.DoubleMetricBuilder doubleMetricBuilder() {
        return DoubleMetric.builder()
            .identifier(METRIC_IDENTIFIER)
            .value(METRIC_VALUE_DOUBLE);
    }
}
