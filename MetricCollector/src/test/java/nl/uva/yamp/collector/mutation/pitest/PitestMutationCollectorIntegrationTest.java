package nl.uva.yamp.collector.mutation.pitest;

import dagger.Component;
import nl.uva.yamp.collector.CollectorTestData;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.yamp.core.model.DataSet;
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
                    .fullyQualifiedMethodName("test.pkg.Direct.call")
                    .mutationOperator("org.pitest.mutationtest.engine.gregor.mutators.returns.PrimitiveReturnsMutator")
                    .lineNumber(7)
                    .killed(false)
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