package nl.uva.yamp.collector;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.TargetDirectory;
import nl.uva.yamp.core.model.TestCase;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectorTestData {

    public static final String TARGET_MODULE = "module-name";
    public static final Path TARGET_PATH = Paths.get(TARGET_MODULE);
    public static final String PACKAGE_NAME = "test.pkg";
    public static final String CLASS_NAME = "UnitTest";
    public static final String METHOD_NAME = "test1";
    public static final String DESCRIPTOR = "()V";
    public static final int MUTATION_SCORE = 0;
    public static final int COVERAGE_LOC = 0;
    public static final boolean DIRECT = false;

    public static TargetDirectory.TargetDirectoryBuilder targetDirectoryBuilder() {
        return TargetDirectory.builder()
            .path(TARGET_PATH)
            .moduleName(TARGET_MODULE);
    }

    public static DataSet.DataSetBuilder dataSetBuilder() {
        return DataSet.builder()
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
            .direct(DIRECT);
    }

    public static Method.MethodBuilder methodBuilder() {
        return Method.builder()
            .packageName(PACKAGE_NAME)
            .className(CLASS_NAME)
            .methodName(METHOD_NAME)
            .descriptor(DESCRIPTOR)
            .loc(COVERAGE_LOC)
            .direct(DIRECT);
    }
}
