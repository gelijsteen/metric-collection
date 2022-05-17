package nl.uva.yamp.coverage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoverageTestData {

    public static final String PACKAGE_NAME = "test.pkg";
    public static final String CLASS_NAME = "Class";
    public static final String METHOD_NAME = "method";

    public static Coverage.CoverageBuilder coverageBuilder() {
        return Coverage.builder()
            .testMethod(methodBuilder().build())
            .constructors(Set.of(constructorBuilder().build()))
            .methods(Set.of(methodBuilder().build()));
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
