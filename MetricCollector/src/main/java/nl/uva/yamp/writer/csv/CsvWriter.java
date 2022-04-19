package nl.uva.yamp.writer.csv;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.uva.yamp.core.Writer;
import nl.uva.yamp.core.model.metric.Metric;
import nl.uva.yamp.core.model.metric.TestMetrics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvWriter implements Writer {

    private final CsvWriterConfiguration configuration;

    @Override
    @SneakyThrows
    public void write(Collection<TestMetrics> testMetrics) {
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(configuration.getOutputFile())))) {
            writeHeaderRow(testMetrics, printWriter);
            testMetrics.forEach(metrics -> writeTestMethodRow(metrics, printWriter));
        }
    }

    private void writeHeaderRow(Collection<TestMetrics> testMetrics, PrintWriter printWriter) {
        List<String> list = new LinkedList<>();
        list.add("TestMethod");
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
    private void writeTestMethodRow(TestMetrics testMetrics, PrintWriter printWriter) {
        List<String> list = new LinkedList<>();
        list.add(testMetrics.getTestMethod().getFullyQualifiedMethodName());
        list.addAll(testMetrics.getMetrics()
            .stream()
            .map(Metric::getStringValue)
            .collect(Collectors.toList()));
        printWriter.println(String.join(",", list));
    }
}
