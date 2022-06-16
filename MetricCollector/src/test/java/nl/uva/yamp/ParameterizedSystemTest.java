package nl.uva.yamp;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.collector.callgraph.javassist.JavassistCallGraphModule;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.yamp.collector.module.maven.MavenTargetModule;
import nl.uva.yamp.collector.mutation.pitest.PitestMutationModule;
import nl.uva.yamp.core.CoreModule;
import nl.uva.yamp.core.CoreTestData;
import nl.uva.yamp.core.model.MetricSet;
import nl.uva.yamp.core.writer.Writer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParameterizedSystemTest {

    @Inject
    public Application sut;

    @Inject
    public Writer writer;

    @Captor
    private ArgumentCaptor<Collection<MetricSet>> captor;

    @BeforeEach
    void setUp() {
        DaggerParameterizedSystemTest_TestComponent.create().inject(this);
    }

    @Test
    void whenParameterizedTests_expectValidResults() {
        sut.run();

        verify(writer, only()).write(captor.capture());
        assertThat(captor.getValue()).containsExactly(
            CoreTestData.metricSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .packageName("test.pkg")
                    .className("UnitTest")
                    .methodName("test1")
                    .identifier("[0]")
                    .build())
                .metrics(List.of(
                    CoreTestData.integerMetricBuilder()
                        .identifier("rTDATA")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("IMC")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("ICC")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("IPC")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("DPHC")
                        .value(1)
                        .build(),
                    CoreTestData.doubleMetricBuilder()
                        .identifier("rDirectness")
                        .value(1d)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("tLOC")
                        .value(8)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("aLOC")
                        .value(2)
                        .build(),
                    CoreTestData.stringMetricBuilder()
                        .identifier("DEV")
                        .value("UT")
                        .build(),
                    CoreTestData.doubleMetricBuilder()
                        .identifier("MutationScore")
                        .value(0.85)
                        .build(),
                    CoreTestData.doubleMetricBuilder()
                        .identifier("disjointMutationScore")
                        .value(1d)
                        .build()))
                .build(),
            CoreTestData.metricSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .packageName("test.pkg")
                    .className("UnitTest")
                    .methodName("test1")
                    .identifier("[1]")
                    .build())
                .metrics(List.of(
                    CoreTestData.integerMetricBuilder()
                        .identifier("rTDATA")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("IMC")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("ICC")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("IPC")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("DPHC")
                        .value(1)
                        .build(),
                    CoreTestData.doubleMetricBuilder()
                        .identifier("rDirectness")
                        .value(1d)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("tLOC")
                        .value(8)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("aLOC")
                        .value(2)
                        .build(),
                    CoreTestData.stringMetricBuilder()
                        .identifier("DEV")
                        .value("UT")
                        .build(),
                    CoreTestData.doubleMetricBuilder()
                        .identifier("MutationScore")
                        .value(0.85)
                        .build(),
                    CoreTestData.doubleMetricBuilder()
                        .identifier("disjointMutationScore")
                        .value(1d)
                        .build()))
                .build());
    }

    @Singleton
    @Component(modules = {
        CoreModule.class,
        MavenTargetModule.class,
        JacocoCoverageModule.class,
        JavassistCallGraphModule.class,
        PitestMutationModule.class,
        MockedWriterModule.class,
        ProjectDirectoryModule.class
    })
    public interface TestComponent {

        void inject(ParameterizedSystemTest parameterizedSystemTest);
    }

    @Module
    public interface MockedWriterModule {

        @Provides
        @Singleton
        static Writer writer() {
            return Mockito.mock(Writer.class);
        }
    }

    @Module
    public interface ProjectDirectoryModule {

        @Provides
        static Path path() {
            return Paths.get("src/test/resources/system-test/parameterized");
        }
    }
}
