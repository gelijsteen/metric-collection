package nl.uva.yamp;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import lombok.SneakyThrows;
import nl.uva.yamp.collector.callgraph.javassist.JavassistCallGraphCollector;
import nl.uva.yamp.core.collector.CallGraphCollector;
import nl.uva.yamp.core.combinator.DatasetCombinator;
import nl.uva.yamp.core.combinator.DefaultDatasetCombinator;
import nl.uva.yamp.core.collector.CoverageCollector;
import nl.uva.yamp.core.filter.Filter;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.metric.MutationScoreMetricCollector;
import nl.uva.yamp.core.metric.UniqueClassesMetricCollector;
import nl.uva.yamp.core.metric.UniqueMethodsMetricCollector;
import nl.uva.yamp.core.metric.UniquePackagesMetricCollector;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.validator.Validator;
import nl.uva.yamp.core.writer.Writer;
import nl.uva.yamp.collector.coverage.CoverageConfiguration;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageCollector;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageConfiguration;
import nl.uva.yamp.collector.mutation.pitest.PitestMutationCollector;
import nl.uva.yamp.util.PathResolver;
import nl.uva.yamp.core.validator.CallGraphValidator;
import nl.uva.yamp.writer.WriterConfiguration;
import nl.uva.yamp.writer.WriterConfiguration.NestedWriterConfiguration;
import nl.uva.yamp.writer.console.ConsoleWriter;
import nl.uva.yamp.writer.csv.CsvWriter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Module
interface ApplicationModule {

    @Provides
    static JacocoCoverageConfiguration jacocoCoverageConfiguration() {
        CoverageConfiguration coverageConfiguration = loadConfiguration(CoverageConfiguration.class);
        return Optional.ofNullable(coverageConfiguration)
            .map(CoverageConfiguration::getCoverage)
            .map(CoverageConfiguration.NestedCoverageConfiguration::getJacoco)
            .orElseThrow();
    }

    @Binds
    CoverageCollector defaultCoverageCollector(JacocoCoverageCollector impl);

    @Binds
    CallGraphCollector defaultCallGraphCollector(JavassistCallGraphCollector impl);

    @Binds
    MutationCollector defaultMutationCollector(PitestMutationCollector impl);

    @Binds
    Validator defaultValidator(CallGraphValidator impl);

    @Binds
    DatasetCombinator defaultDatasetCombinator(DefaultDatasetCombinator impl);

    @Provides
    static List<MetricCollector> metricCollector(UniquePackagesMetricCollector uniquePackagesMetricCollector,
                                                 UniqueClassesMetricCollector uniqueClassesMetricCollector,
                                                 UniqueMethodsMetricCollector uniqueMethodsMetricCollector,
                                                 MutationScoreMetricCollector mutationScoreMetricCollector) {
        return List.of(
            uniquePackagesMetricCollector,
            uniqueClassesMetricCollector,
            uniqueMethodsMetricCollector,
            mutationScoreMetricCollector);
    }

    @Provides
    static List<Filter> filters() {
        return List.of();
    }

    @Provides
    static List<Writer> writers() {
        WriterConfiguration writerConfiguration = loadConfiguration(WriterConfiguration.class);
        Optional<NestedWriterConfiguration> nestedWriterConfiguration = Optional.ofNullable(writerConfiguration)
            .map(WriterConfiguration::getWriter);

        return Stream.of(nestedWriterConfiguration.map(NestedWriterConfiguration::getConsole)
                    .map(configuration -> new ConsoleWriter()),
                nestedWriterConfiguration.map(NestedWriterConfiguration::getCsv)
                    .map(CsvWriter::new))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
    }

    @SneakyThrows
    private static <T> T loadConfiguration(Class<T> clazz) {
        Path path = PathResolver.getPath("application.yml");
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            return new Yaml(representer).loadAs(bufferedReader, clazz);
        }
    }
}
