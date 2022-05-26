package nl.uva.yamp.collector.callgraph.javassist;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.collector.CollectorTestData;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.yamp.core.model.Coverage;
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
        Coverage result = sut.collect(CollectorTestData.coverageBuilder()
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

        assertThat(result).isEqualTo(CollectorTestData.coverageBuilder()
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