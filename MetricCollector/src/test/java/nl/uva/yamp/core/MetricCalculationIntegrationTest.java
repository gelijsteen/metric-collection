package nl.uva.yamp.core;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.core.collector.CallGraphCollector;
import nl.uva.yamp.core.collector.CoverageCollector;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.model.metric.TestMetrics;
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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MetricCalculationIntegrationTest {

    @Inject
    public MetricCalculation sut;

    @Inject
    public Writer writer;

    @Captor
    private ArgumentCaptor<Collection<TestMetrics>> captor;

    @BeforeEach
    void setUp() {
        DaggerMetricCalculationIntegrationTest_TestComponent.create().inject(this);
    }

    @Test
    void happyFlow() {
        sut.calculate();

        verify(writer, only()).write(captor.capture());
        assertThat(captor.getValue()).containsExactly(
            CoreTestData.testMetricsBuilder()
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
                        .value(0.0)
                        .build(),
                    CoreTestData.integerMetricBuilder()
                        .identifier("MutationScore")
                        .value(100)
                        .build()))
                .build());
    }

    @Singleton
    @Component(modules = {
        CoreModule.class,
        FakeCoverageModule.class,
        FakeCallGraphModule.class,
        FakeMutationModule.class,
        MockedWriterModule.class
    })
    public interface TestComponent {

        void inject(MetricCalculationIntegrationTest metricCalculationIntegrationTest);
    }

    @Module
    public interface FakeCoverageModule {

        @Provides
        static CoverageCollector coverageCollector() {
            return () -> Set.of(CoreTestData.coverageBuilder().build());
        }
    }

    @Module
    public interface FakeCallGraphModule {

        @Provides
        static CallGraphCollector callGraphCollector() {
            return (coverage) -> CoreTestData.callGraphBuilder().build();
        }
    }

    @Module
    public interface FakeMutationModule {

        @Provides
        static MutationCollector mutationCollector() {
            return (coverage) -> CoreTestData.mutationBuilder().build();
        }
    }

    @Module
    public interface MockedWriterModule {

        @Provides
        @Singleton
        static Writer writer() {
            return Mockito.mock(Writer.class);
        }
    }
}