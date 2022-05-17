package nl.uva.yamp.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.Mutation;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoreTestData {

    public static final int MUTATION_SCORE = 100;
    public static final String PACKAGE_NAME = "a.b.c";
    public static final String CLASS_NAME = "D";
    public static final String METHOD_NAME = "e";

    public static Coverage.CoverageBuilder coverageBuilder() {
        return Coverage.builder()
            .testMethod(methodBuilder().build())
            .constructors(Set.of(constructorBuilder().build()))
            .methods(Set.of(methodBuilder().build()));
    }

    public static Mutation.MutationBuilder mutationBuilder() {
        return Mutation.builder()
            .testMethod(methodBuilder().build())
            .mutationScore(MUTATION_SCORE);
    }

    public static CombinedData.CombinedDataBuilder combinedDataBuilder() {
        return CombinedData.builder()
            .testMethod(methodBuilder().build())
            .constructors(Set.of(constructorBuilder().build()))
            .methods(Set.of(methodBuilder().build()))
            .mutationScore(MUTATION_SCORE);
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
