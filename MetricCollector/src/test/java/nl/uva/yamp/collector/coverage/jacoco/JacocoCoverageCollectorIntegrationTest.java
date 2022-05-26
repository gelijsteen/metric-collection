package nl.uva.yamp.collector.coverage.jacoco;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.collector.CollectorTestData;
import nl.uva.yamp.core.model.DataSet;
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
        Set<DataSet> result = sut.collect();

        assertThat(result).containsExactlyInAnyOrder(
            CollectorTestData.dataSetBuilder()
                .testCase(CollectorTestData.testCaseBuilder().build())
                .constructors(Set.of(
                    CollectorTestData.constructorBuilder()
                        .className("Direct")
                        .loc(1)
                        .build(),
                    CollectorTestData.constructorBuilder()
                        .className("Indirect")
                        .loc(1)
                        .build()
                ))
                .methods(Set.of(
                    CollectorTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .descriptor("(I)I")
                        .loc(2)
                        .build(),
                    CollectorTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .descriptor("(I)I")
                        .loc(1)
                        .build()
                ))
                .testConstructors(Set.of(CollectorTestData.constructorBuilder()
                    .loc(2)
                    .build()))
                .testMethods(Set.of(CollectorTestData.methodBuilder()
                    .loc(3)
                    .build()))
                .build(),
            CollectorTestData.dataSetBuilder()
                .testCase(CollectorTestData.testCaseBuilder()
                    .methodName("test2")
                    .build())
                .constructors(Set.of(
                    CollectorTestData.constructorBuilder()
                        .className("Direct")
                        .loc(1)
                        .build(),
                    CollectorTestData.constructorBuilder()
                        .className("Indirect")
                        .loc(1)
                        .build()
                ))
                .methods(Set.of(
                    CollectorTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .descriptor("(I)I")
                        .loc(2)
                        .build(),
                    CollectorTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .descriptor("(I)I")
                        .loc(1)
                        .build()
                ))
                .testConstructors(Set.of(CollectorTestData.constructorBuilder()
                    .loc(2)
                    .build()))
                .testMethods(Set.of(CollectorTestData.methodBuilder()
                    .methodName("test2")
                    .loc(3)
                    .build()))
                .build(),
            CollectorTestData.dataSetBuilder()
                .testCase(CollectorTestData.testCaseBuilder()
                    .methodName("test3")
                    .build())
                .constructors(Set.of(
                    CollectorTestData.constructorBuilder()
                        .className("Direct")
                        .loc(1)
                        .build(),
                    CollectorTestData.constructorBuilder()
                        .className("Indirect")
                        .loc(1)
                        .build()
                ))
                .methods(Set.of(
                    CollectorTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .descriptor("(I)I")
                        .loc(2)
                        .build(),
                    CollectorTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .descriptor("(I)I")
                        .loc(1)
                        .build()
                ))
                .testConstructors(Set.of(CollectorTestData.constructorBuilder()
                    .loc(2)
                    .build()))
                .testMethods(Set.of(CollectorTestData.methodBuilder()
                    .methodName("test3")
                    .loc(3)
                    .build()))
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
            return Paths.get("src/test/resources/reference");
        }
    }
}