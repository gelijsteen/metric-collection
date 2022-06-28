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
class BasicSystemTest {

    @Inject
    public Application sut;

    @Inject
    public Writer writer;

    @Captor
    private ArgumentCaptor<Collection<MetricSet>> captor;

    @BeforeEach
    void setUp() {
        DaggerBasicSystemTest_TestComponent.create().inject(this);
    }

    @Test
    void happyFlow() {
        sut.run();

        verify(writer, only()).write(captor.capture());
        assertThat(captor.getValue()).containsExactly(
            CoreTestData.metricSetBuilder()
                .testCase(CoreTestData.testCaseBuilder()
                    .packageName("test.pkg")
                    .className("UnitTest")
                    .methodName("test1")
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
                        .value(5)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("aLOC")
                        .value(2)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("UT")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("NKM")
                        .value(17)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("NSM")
                        .value(3)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("dNKM")
                        .value(1)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("dNSM")
                        .value(0)
                        .build(),
                    CoreTestData.doubleMetricBuilder()
                        .identifier("MutationScore")
                        .value(0.85)
                        .build(),
                    CoreTestData.doubleMetricBuilder()
                        .identifier("dMutationScore")
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

        void inject(BasicSystemTest basicSystemTest);
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
            return Paths.get("src/test/resources/system-test/basic");
        }
    }
}
