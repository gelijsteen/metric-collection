package nl.uva.yamp.collector.coverage.jacoco;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.collector.coverage.CoverageTestData;
import nl.uva.yamp.core.model.Coverage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JacocoCoverageCollectorIntegrationTest {

    @Inject
    public JacocoCoverageCollector sut;

    @BeforeEach
    void setUp() {
        DaggerJacocoCoverageCollectorIntegrationTest_TestComponent.create().inject(this);
    }

    @Test
    void happyFlow() {
        Set<Coverage> result = sut.collect();

        assertThat(result).containsExactlyInAnyOrder(
            CoverageTestData.coverageBuilder()
                .testCase(CoverageTestData.testCaseBuilder()
                    .className("UnitTest")
                    .methodName("test1")
                    .build())
                .constructors(Set.of(
                    CoverageTestData.constructorBuilder()
                        .className("Direct")
                        .build(),
                    CoverageTestData.constructorBuilder()
                        .className("Indirect")
                        .build()
                ))
                .methods(Set.of(
                    CoverageTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    CoverageTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build()
                ))
                .build(),
            CoverageTestData.coverageBuilder()
                .testCase(CoverageTestData.testCaseBuilder()
                    .className("UnitTest")
                    .methodName("test2")
                    .build())
                .constructors(Set.of(
                    CoverageTestData.constructorBuilder()
                        .className("Direct")
                        .build(),
                    CoverageTestData.constructorBuilder()
                        .className("Indirect")
                        .build()
                ))
                .methods(Set.of(
                    CoverageTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    CoverageTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build()
                ))
                .build(),
            CoverageTestData.coverageBuilder()
                .testCase(CoverageTestData.testCaseBuilder()
                    .className("UnitTest")
                    .methodName("test3")
                    .build())
                .constructors(Set.of(
                    CoverageTestData.constructorBuilder()
                        .className("Direct")
                        .build(),
                    CoverageTestData.constructorBuilder()
                        .className("Indirect")
                        .build()
                ))
                .methods(Set.of(
                    CoverageTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    CoverageTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build()
                ))
                .build()
        );
    }

    @Component(modules = {
        JacocoCoverageModule.class,
        ProjectDirectoryModule.class
    })
    public interface TestComponent {

        void inject(JacocoCoverageCollectorIntegrationTest jacocoCoverageCollectorIntegrationTest);
    }

    @Module
    public interface ProjectDirectoryModule {

        @Provides
        static Path path() {
            return Paths.get("src/test/resources");
        }
    }
}