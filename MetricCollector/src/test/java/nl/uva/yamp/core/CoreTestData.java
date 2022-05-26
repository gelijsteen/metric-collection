package nl.uva.yamp.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.TestCase;
import nl.uva.yamp.core.model.metric.DoubleMetric;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.TestMetrics;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoreTestData {

    public static final String PACKAGE_NAME = "a.b.c";
    public static final String CLASS_NAME = "D";
    public static final String METHOD_NAME = "e";
    public static final String DESCRIPTOR = "()V";
    public static final int MUTATION_SCORE = 100;
    public static final int COVERAGE_LOC = 0;
    public static final String METRIC_IDENTIFIER = "identifier";
    public static final int METRIC_VALUE_INTEGER = 0;
    public static final double METRIC_VALUE_DOUBLE = 0d;

    public static Coverage.CoverageBuilder coverageBuilder() {
        return Coverage.builder()
            .testCase(testCaseBuilder().build())
            .mutationScore(MUTATION_SCORE)
            .constructors(Set.of(constructorBuilder().build()))
            .methods(Set.of(methodBuilder().build()))
            .testConstructors(Set.of(constructorBuilder().build()))
            .testMethods(Set.of(methodBuilder().build()));
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
            .className(CLASS_NAME)
            .descriptor(DESCRIPTOR)
            .loc(COVERAGE_LOC)
            .direct(true);
    }

    public static Method.MethodBuilder methodBuilder() {
        return Method.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME)
            .descriptor(DESCRIPTOR)
            .loc(COVERAGE_LOC)
            .direct(false);
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
