package nl.uva.yamp.collector.callgraph.javassist;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.collector.CollectorTestData;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.yamp.core.model.CallGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.nio.file.Path;
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
        CallGraph result = sut.collect(CollectorTestData.coverageBuilder()
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
            .build());

        assertThat(result).isEqualTo(CollectorTestData.callGraphBuilder()
            .constructors(Set.of())
            .methods(Set.of(CollectorTestData.callGraphMethodBuilder()
                .method(CollectorTestData.methodBuilder()
                    .className("Direct")
                    .methodName("call")
                    .build())
                .constructors(Set.of(CollectorTestData.callGraphConstructorBuilder()
                    .constructor(CollectorTestData.constructorBuilder()
                        .className("Indirect")
                        .build())
                    .build()))
                .methods(Set.of(CollectorTestData.callGraphMethodBuilder()
                    .method(CollectorTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build())
                    .build()))
                .build()))
            .build());
    }

    @Component(modules = {
        JacocoCoverageModule.class,
        ProjectDirectoryModule.class
    })
    public interface TestComponent {

        void inject(JavassistCallGraphCollectorIntegrationTest javassistCallGraphCollectorIntegrationTest);
    }

    @Module
    public interface ProjectDirectoryModule {

        @Provides
        static Path path() {
            return Paths.get("src/test/resources/reference");
        }
    }
}