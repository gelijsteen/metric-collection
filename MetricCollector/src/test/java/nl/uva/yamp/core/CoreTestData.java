package nl.uva.yamp.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoreTestData {

    public static final String PACKAGE_NAME = "a.b.c";
    public static final String CLASS_NAME = "D";
    public static final String METHOD_NAME = "e";

    public static Coverage.CoverageBuilder coverageBuilder() {
        return Coverage.builder()
            .testMethod(methodBuilder().build())
            .coveredMethods(Set.of(methodBuilder().build()));
    }

    public static Method.MethodBuilder methodBuilder() {
        return Method.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME);
    }
}
