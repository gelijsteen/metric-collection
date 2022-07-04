package nl.uva.meco.collector.callgraph.javassist;

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

class JavassistCallGraphCollectorIntegrationTest {

    @Inject
    public JavassistCallGraphCollector sut;

    @BeforeEach
    void setUp() {
        DaggerJavassistCallGraphCollectorIntegrationTest_TestComponent.create().inject(this);
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
                        .descriptor("(I)I")
                        .build(),
                    CollectorTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .descriptor("(I)I")
                        .build()
                ))
                .build());

        assertThat(result).isEqualTo(CollectorTestData.dataSetBuilder()
            .constructors(Set.of(
                CollectorTestData.constructorBuilder()
                    .className("Direct")
                    .direct(true)
                    .build(),
                CollectorTestData.constructorBuilder()
                    .className("Indirect")
                    .direct(false)
                    .build()
            ))
            .methods(Set.of(
                CollectorTestData.methodBuilder()
                    .className("Direct")
                    .methodName("call")
                    .descriptor("(I)I")
                    .direct(true)
                    .build(),
                CollectorTestData.methodBuilder()
                    .className("Indirect")
                    .methodName("call")
                    .descriptor("(I)I")
                    .direct(false)
                    .build()
            ))
            .build());
    }

    @Component(modules = {
        JacocoCoverageModule.class
    })
    public interface TestComponent {

        void inject(JavassistCallGraphCollectorIntegrationTest javassistCallGraphCollectorIntegrationTest);
    }
}