package nl.uva.meco.collector.mutation.pitest;

import dagger.Component;
import nl.uva.meco.collector.CollectorTestData;
import nl.uva.meco.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.meco.core.model.DataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PitestMutationCollectorIntegrationTest {

    @Inject
    public PitestMutationCollector sut;

    @BeforeEach
    void setUp() {
        DaggerPitestMutationCollectorIntegrationTest_TestComponent.create().inject(this);
    }

    @Test
    void happyFlow() {
        DataSet result = sut.collect(CollectorTestData.targetDirectoryBuilder()
                .path(Paths.get("src/test/resources/reference/target"))
                .build(),
            CollectorTestData.dataSetBuilder()
                .testCase(CollectorTestData.testCaseBuilder().build())
                .constructors(Set.of(
                    CollectorTestData.constructorBuilder()
                        .className("Direct")
                        .build(),
                    CollectorTestData.constructorBuilder()
                        .className("Indirect")
                        .build()
                ))
                .methods(Set.of(
                    CollectorTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    CollectorTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build()
                ))
                .testConstructors(Set.of(
                    CollectorTestData.constructorBuilder()
                        .build()
                ))
                .testMethods(Set.of(
                    CollectorTestData.methodBuilder()
                        .methodName("test1")
                        .build()
                ))
                .build());

        assertThat(result).isEqualTo(CollectorTestData.dataSetBuilder()
            .constructors(Set.of(
                CollectorTestData.constructorBuilder()
                    .className("Direct")
                    .build(),
                CollectorTestData.constructorBuilder()
                    .className("Indirect")
                    .build()
            ))
            .methods(Set.of(
                CollectorTestData.methodBuilder()
                    .className("Direct")
                    .methodName("call")
                    .build(),
                CollectorTestData.methodBuilder()
                    .className("Indirect")
                    .methodName("call")
                    .build()
            ))
            .testConstructors(Set.of(
                CollectorTestData.constructorBuilder()
                    .build()
            ))
            .testMethods(Set.of(
                CollectorTestData.methodBuilder()
                    .methodName("test1")
                    .build()
            ))
            .mutations(Set.of(
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.returns.PrimitiveReturnsMutator")
                    .lineNumber(6)
                    .killed(false)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.MathMutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.InlineConstantMutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.UOI1Mutator")
                    .lineNumber(6)
                    .killed(false)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.UOI2Mutator")
                    .lineNumber(6)
                    .killed(false)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.UOI3Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.UOI4Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.AOR1Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.AOR2Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.AOR3Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.AOR4Mutator")
                    .lineNumber(6)
                    .killed(false)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.CRCR2Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.CRCR3Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.CRCR4Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.CRCR5Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.CRCR6Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.AOD1Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.AOD2Mutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Indirect.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.ABSMutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),

                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.returns.PrimitiveReturnsMutator")
                    .lineNumber(7)
                    .killed(false)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.ConstructorCallMutator")
                    .lineNumber(6)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.NonVoidMethodCallMutator")
                    .lineNumber(7)
                    .killed(false)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.experimental.ArgumentPropagationMutator")
                    .lineNumber(7)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.UOI1Mutator")
                    .lineNumber(7)
                    .killed(false)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.UOI2Mutator")
                    .lineNumber(7)
                    .killed(false)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.UOI3Mutator")
                    .lineNumber(7)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.UOI4Mutator")
                    .lineNumber(7)
                    .killed(true)
                    .build(),
                CollectorTestData.mutationBuilder()
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.rv.ABSMutator")
                    .lineNumber(7)
                    .killed(true)
                    .build()
            ))
            .build());
    }

    @Component(modules = {
        JacocoCoverageModule.class
    })
    public interface TestComponent {

        void inject(PitestMutationCollectorIntegrationTest pitestMutationCollectorIntegrationTest);
    }
}