package nl.uva.yamp.collector;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CallGraph;
import nl.uva.yamp.core.model.CallGraphConstructor;
import nl.uva.yamp.core.model.CallGraphMethod;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.CoverageConstructor;
import nl.uva.yamp.core.model.CoverageMethod;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.TestCase;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectorTestData {

    public static final int MUTATION_SCORE = 100;
    public static final String PACKAGE_NAME = "test.pkg";
    public static final String CLASS_NAME = "UnitTest";
    public static final String METHOD_NAME = "test1";
    public static final String DESCRIPTOR = "()V";
    public static final int COVERAGE_LOC = 0;

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
}
