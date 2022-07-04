package nl.uva.meco.collector.module.maven;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import nl.uva.meco.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.meco.core.model.TargetDirectory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MavenTargetCollectorIntegrationTest {

    @Inject
    public MavenTargetCollector sut;

    @BeforeEach
    void setUp() {
        DaggerMavenTargetCollectorIntegrationTest_TestComponent.create().inject(this);
    }

    @Test
    void happyFlow() {
        Set<TargetDirectory> result = sut.collect();

        assertThat(result).containsExactly(TargetDirectory.builder()
            .path(Paths.get("src/test/resources/reference/target"))
            .moduleName("reference")
            .build());
    }

    @Component(modules = {
        JacocoCoverageModule.class,
        ProjectDirectoryModule.class
    })
    public interface TestComponent {

        void inject(MavenTargetCollectorIntegrationTest mavenTargetCollectorIntegrationTest);
    }

    @Module
    public interface ProjectDirectoryModule {

        @Provides
        static Path path() {
            return Paths.get("src/test/resources/reference");
        }
    }
}