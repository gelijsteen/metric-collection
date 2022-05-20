package nl.uva.yamp.collector;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.TestCase;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectorTestData {

    public static final String PACKAGE_NAME = "test.pkg";
    public static final String CLASS_NAME = "UnitTest";
    public static final String METHOD_NAME = "test1";

    public static Coverage.CoverageBuilder coverageBuilder() {
        return Coverage.builder()
            .testCase(testCaseBuilder().build())
            .constructors(Set.of(constructorBuilder().build()))
            .methods(Set.of(methodBuilder().build()));
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
}
