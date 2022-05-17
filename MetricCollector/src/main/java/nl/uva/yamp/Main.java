package nl.uva.yamp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.MetricCalculation;
import nl.uva.yamp.core.combinator.DatasetCombinator;
import nl.uva.yamp.core.combinator.DefaultDatasetCombinator;
import nl.uva.yamp.core.coverage.CoverageReader;
import nl.uva.yamp.core.filter.Filter;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.metric.MutationScoreMetricCollector;
import nl.uva.yamp.core.metric.UniqueClassesMetricCollector;
import nl.uva.yamp.core.metric.UniqueMethodsMetricCollector;
import nl.uva.yamp.core.metric.UniquePackagesMetricCollector;
import nl.uva.yamp.core.mutation.MutationReader;
import nl.uva.yamp.core.writer.Writer;
import nl.uva.yamp.coverage.CoverageConfiguration;
import nl.uva.yamp.coverage.jacoco.ClassFileLoader;
import nl.uva.yamp.coverage.jacoco.JacocoCoverageReader;
import nl.uva.yamp.coverage.jacoco.JacocoFileParser;
import nl.uva.yamp.coverage.jacoco.TargetDirectoryLocator;
import nl.uva.yamp.mutation.MutationConfiguration;
import nl.uva.yamp.mutation.pitest.PitestMutationReader;
import nl.uva.yamp.writer.WriterConfiguration;
import nl.uva.yamp.writer.console.ConsoleWriter;
import nl.uva.yamp.writer.csv.CsvWriter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            log.info("Usage: <file.yml>");
            return;
        }

        CoverageReader coverageReader = wireCoverageReader(args[0], args.length >= 2 ? args[1] : null);
        MutationReader mutationReader = wireMutationReader(args[0], args.length >= 2 ? args[1] : null);
        DatasetCombinator datasetCombinator = new DefaultDatasetCombinator();
        List<Filter> filters = List.of();
        List<MetricCollector> metricCollectors = wireMetricCollectors();
        List<Writer> writers = wireWriters(args[0], args.length >= 3 ? args[2] : null);

        new MetricCalculation(coverageReader, mutationReader, datasetCombinator, filters, metricCollectors, writers).calculate();
    }

    private static CoverageReader wireCoverageReader(String configurationFile, String override) {
        CoverageConfiguration coverageConfiguration = loadConfiguration(configurationFile, CoverageConfiguration.class);
        Optional.ofNullable(override).ifPresent(v -> coverageConfiguration.getCoverage().getJacoco().setProjectDirectory(v));
        return Optional.ofNullable(coverageConfiguration.getCoverage().getJacoco())
            .map(jacocoReaderConfiguration -> {
                JacocoFileParser jacocoFileParser = new JacocoFileParser();
                TargetDirectoryLocator targetDirectoryLocator = new TargetDirectoryLocator();
                ClassFileLoader classFileLoader = new ClassFileLoader();
                return new JacocoCoverageReader(coverageConfiguration.getCoverage().getJacoco(), jacocoFileParser, targetDirectoryLocator, classFileLoader);
            })
            .orElseThrow(() -> new IllegalArgumentException("Required coverage configuration missing."));
    }

    private static MutationReader wireMutationReader(String configurationFile, String override) {
        MutationConfiguration mutationConfiguration = loadConfiguration(configurationFile, MutationConfiguration.class);
        Optional.ofNullable(override).ifPresent(v -> mutationConfiguration.getMutation().getPitest().setProjectDirectory(v));
        return Optional.ofNullable(mutationConfiguration.getMutation().getPitest())
            .map(jacocoReaderConfiguration -> new PitestMutationReader(mutationConfiguration.getMutation().getPitest()))
            .orElseThrow(() -> new IllegalArgumentException("Required mutation configuration missing."));
    }

    private static List<MetricCollector> wireMetricCollectors() {
        return List.of(
            new UniquePackagesMetricCollector(),
            new UniqueClassesMetricCollector(),
            new UniqueMethodsMetricCollector(),
            new MutationScoreMetricCollector());
    }

    private static List<Writer> wireWriters(String configurationFile, String override) {
        WriterConfiguration writerConfiguration = loadConfiguration(configurationFile, WriterConfiguration.class);
        Optional.ofNullable(override).ifPresent(v -> writerConfiguration.getWriter().getCsv().setOutputFile(v));
        return Stream.of(
                Optional.ofNullable(writerConfiguration.getWriter().getCsv()).map(CsvWriter::new),
                Optional.ofNullable(writerConfiguration.getWriter().getConsole()).map(configuration -> new ConsoleWriter()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    @SneakyThrows
    private static <T> T loadConfiguration(String path, Class<T> clazz) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path))) {
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            return new Yaml(representer).loadAs(bufferedReader, clazz);
        }
    }
}
