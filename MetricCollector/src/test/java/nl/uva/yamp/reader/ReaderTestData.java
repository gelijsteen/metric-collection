package nl.uva.yamp.reader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReaderTestData {

    public static final String PACKAGE_NAME = "test.pkg";
    public static final String CLASS_NAME = "Class";
    public static final String METHOD_NAME = "method";

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
