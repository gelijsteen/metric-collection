package nl.uva.yamp.collector.mutation.pitest;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.collector.CollectorTestData;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.yamp.core.model.Mutation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.nio.file.Path;
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
        Mutation result = sut.collect(CollectorTestData.coverageBuilder()
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
                    .build(),
                CollectorTestData.coverageMethodBuilder()
                    .className("Indirect")
                    .methodName("call")
                    .build()
            ))
            .build());

        assertThat(result).isEqualTo(CollectorTestData.mutationBuilder()
            .mutationScore(33)
            .build());
    }

    @Component(modules = {
        JacocoCoverageModule.class,
        ProjectDirectoryModule.class
    })
    public interface TestComponent {

        void inject(PitestMutationCollectorIntegrationTest pitestMutationCollectorIntegrationTest);
    }

    @Module
    public interface ProjectDirectoryModule {

        @Provides
        static Path path() {
            return Paths.get("src/test/resources/reference");
        }
    }
}