package nl.uva.yamp.writer.csv;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.uva.yamp.core.model.metric.Metric;
import nl.uva.yamp.core.model.metric.TestMetrics;
import nl.uva.yamp.core.writer.Writer;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class CsvWriter implements Writer {

    private final CsvWriterConfiguration configuration;

    @Override
    @SneakyThrows
    public void write(Collection<TestMetrics> testMetrics) {
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(configuration.getOutputFile())))) {
            writeHeaderRow(testMetrics, printWriter);
            testMetrics.forEach(metrics -> writeTestCaseRow(metrics, printWriter));
        }
    }

    private void writeHeaderRow(Collection<TestMetrics> testMetrics, PrintWriter printWriter) {
        List<String> list = new LinkedList<>();
        list.add("TestCase");
        list.addAll(testMetrics.stream()
            .findFirst()
            .map(TestMetrics::getMetrics)
            .map(metrics -> metrics
                .stream()
                .map(Metric::getIdentifier)
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList()));
        printWriter.println(String.join(",", list));
    }

    @SneakyThrows
    private void writeTestCaseRow(TestMetrics testMetrics, PrintWriter printWriter) {
        List<String> list = new LinkedList<>();
        list.add(testMetrics.getTestCase()
            .getFullyQualifiedMethodName()
            .replace(',', ';')
            .replace('\n', ';'));
        list.addAll(testMetrics.getMetrics()
            .stream()
            .map(Metric::getStringValue)
            .collect(Collectors.toList()));
        printWriter.println(String.join(",", list));
    }
}
