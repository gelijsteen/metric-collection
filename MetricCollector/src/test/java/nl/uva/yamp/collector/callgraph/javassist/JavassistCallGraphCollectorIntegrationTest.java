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
                CollectorTestData.coverageConstructorBuilder()
                    .className("Direct")
                    .build(),
                CollectorTestData.coverageConstructorBuilder()
                    .className("Indirect")
                    .build()
            ))
            .methods(Set.of(
                CollectorTestData.coverageMethodBuilder()
                    .className("Direct")
                    .methodName("call")
                    .descriptor("(I)I")
                    .build(),
                CollectorTestData.coverageMethodBuilder()
                    .className("Indirect")
                    .methodName("call")
                    .descriptor("(I)I")
                    .build()
            ))
            .build());

        assertThat(result).isEqualTo(CollectorTestData.callGraphBuilder()
            .constructors(Set.of(CollectorTestData.callGraphConstructorBuilder()
                .className("Direct")
                .build()))
            .methods(Set.of(CollectorTestData.callGraphMethodBuilder()
                .className("Direct")
                .methodName("call")
                .descriptor("(I)I")
                .constructors(Set.of(CollectorTestData.callGraphConstructorBuilder()
                    .className("Indirect")
                    .build()))
                .methods(Set.of(CollectorTestData.callGraphMethodBuilder()
                    .className("Indirect")
                    .methodName("call")
                    .descriptor("(I)I")
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