package nl.uva.yamp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.MetricCalculation;
import nl.uva.yamp.core.Reader;
import nl.uva.yamp.core.Writer;
import nl.uva.yamp.core.filter.ConstructorCoverageFilter;
import nl.uva.yamp.core.filter.CoverageFilter;
import nl.uva.yamp.core.filter.MethodCommaCoverageFilter;
import nl.uva.yamp.core.metric.MetricCollector;
import nl.uva.yamp.core.metric.UniqueClassesMetricCollector;
import nl.uva.yamp.core.metric.UniqueMethodsMetricCollector;
import nl.uva.yamp.core.metric.UniquePackagesMetricCollector;
import nl.uva.yamp.reader.ReaderConfiguration;
import nl.uva.yamp.reader.jacoco.ClassFileLoader;
import nl.uva.yamp.reader.jacoco.JacocoFileParser;
import nl.uva.yamp.reader.jacoco.JacocoReader;
import nl.uva.yamp.reader.jacoco.TargetDirectoryLocator;
import nl.uva.yamp.writer.WriterConfiguration;
import nl.uva.yamp.writer.console.ConsoleWriter;
import nl.uva.yamp.writer.csv.CsvWriter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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

        Reader reader = wireReader(args[0], args.length >= 2 ? args[1] : null);
        List<CoverageFilter> coverageFilters = wireCoverageFilters();
        List<MetricCollector> metricCollectors = wireMetricCollectors();
        List<Writer> writers = wireWriters(args[0], args.length >= 3 ? args[2] : null);

        new MetricCalculation(reader, coverageFilters, metricCollectors, writers).calculate();
    }

    private static Reader wireReader(String configurationFile, String override) {
        ReaderConfiguration readerConfiguration = loadConfiguration(configurationFile, ReaderConfiguration.class);
        Optional.ofNullable(override).ifPresent(v -> readerConfiguration.getReader().getJacoco().setProjectDirectory(v));
        return Optional.ofNullable(readerConfiguration.getReader().getJacoco())
            .map(jacocoReaderConfiguration -> {
                JacocoFileParser jacocoFileParser = new JacocoFileParser();
                TargetDirectoryLocator targetDirectoryLocator = new TargetDirectoryLocator();
                ClassFileLoader classFileLoader = new ClassFileLoader();
                return new JacocoReader(readerConfiguration.getReader().getJacoco(), jacocoFileParser, targetDirectoryLocator, classFileLoader);
            })
            .orElseThrow(() -> new IllegalArgumentException("Required reader configuration missing."));
    }

    private static List<CoverageFilter> wireCoverageFilters() {
        return Arrays.asList(
            new MethodCommaCoverageFilter(),
            new ConstructorCoverageFilter());
    }

    private static List<MetricCollector> wireMetricCollectors() {
        return Arrays.asList(
            new UniquePackagesMetricCollector(),
            new UniqueClassesMetricCollector(),
            new UniqueMethodsMetricCollector());
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
