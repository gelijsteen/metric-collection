package nl.uva.yamp.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.MetricSet;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.TargetDirectory;
import nl.uva.yamp.core.model.TestCase;
import nl.uva.yamp.core.model.metric.DoubleMetric;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.LongMetric;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoreTestData {

    public static final String TARGET_MODULE = "module-name";
    public static final Path TARGET_PATH = Paths.get(TARGET_MODULE);
    public static final String PACKAGE_NAME = "a.b.c";
    public static final String CLASS_NAME = "D";
    public static final String METHOD_NAME = "e";
    public static final String DESCRIPTOR = "()V";
    public static final int COVERAGE_LOC = 1;
    public static final String METRIC_IDENTIFIER = "identifier";
    public static final int METRIC_VALUE_INTEGER = 0;
    public static final long METRIC_VALUE_LONG = 0;
    public static final double METRIC_VALUE_DOUBLE = 0d;
    public static final String MUTATION_OPERATOR = "mutation-operator";
    public static final String MUTATION_FQN = PACKAGE_NAME + "." + CLASS_NAME + "." + METHOD_NAME;
    public static final int MUTATION_LINE_NUMBER = 10;
    public static final boolean MUTATION_KILLED = false;
    public static final boolean MUTATION_DISJOINT = false;
    public static final boolean CONSTRUCTOR_DIRECT = true;
    public static final boolean METHOD_DIRECT = true;

    public static TargetDirectory.TargetDirectoryBuilder targetDirectoryBuilder() {
        return TargetDirectory.builder()
            .path(TARGET_PATH)
            .moduleName(TARGET_MODULE);
    }

    public static DataSet.DataSetBuilder dataSetBuilder() {
        return DataSet.builder()
            .testCase(testCaseBuilder().build())
            .constructors(Set.of(constructorBuilder().build()))
            .methods(Set.of(methodBuilder().build()))
            .testConstructors(Set.of(constructorBuilder().build()))
            .testMethods(Set.of(methodBuilder().build()))
            .mutations(Set.of());
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
            .direct(CONSTRUCTOR_DIRECT);
    }

    public static Method.MethodBuilder methodBuilder() {
        return Method.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME)
            .descriptor(DESCRIPTOR)
            .loc(COVERAGE_LOC)
            .direct(METHOD_DIRECT);
    }

    public static MetricSet.MetricSetBuilder metricSetBuilder() {
        return MetricSet.builder()
            .testCase(testCaseBuilder().build())
            .metrics(List.of(integerMetricBuilder().build()));
    }

    public static IntegerMetric.IntegerMetricBuilder integerMetricBuilder() {
        return IntegerMetric.builder()
            .identifier(METRIC_IDENTIFIER)
            .value(METRIC_VALUE_INTEGER);
    }

    public static LongMetric.LongMetricBuilder longMetricBuilder() {
        return LongMetric.builder()
            .identifier(METRIC_IDENTIFIER)
            .value(METRIC_VALUE_LONG);
    }

    public static DoubleMetric.DoubleMetricBuilder doubleMetricBuilder() {
        return DoubleMetric.builder()
            .identifier(METRIC_IDENTIFIER)
            .value(METRIC_VALUE_DOUBLE);
    }

    public static Mutation.MutationBuilder mutationBuilder() {
        return Mutation.builder()
            .fullyQualifiedMethodName(MUTATION_FQN)
            .mutationOperator(MUTATION_OPERATOR)
            .lineNumber(MUTATION_LINE_NUMBER)
            .killed(MUTATION_KILLED)
            .disjoint(MUTATION_DISJOINT);
    }
}
