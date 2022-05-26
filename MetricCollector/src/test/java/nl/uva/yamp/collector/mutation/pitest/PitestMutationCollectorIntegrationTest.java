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
            .mutationScore(33)
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
    }

    @Component(modules = {
        JacocoCoverageModule.class
    })
    public interface TestComponent {

        void inject(PitestMutationCollectorIntegrationTest pitestMutationCollectorIntegrationTest);
    }
}