package nl.uva.yamp.writer.csv;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.model.MetricSet;
import nl.uva.yamp.core.model.metric.Metric;
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

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class CsvWriter implements Writer {

    private final CsvWriterConfiguration configuration;

    @Override
    @SneakyThrows
    public void write(Collection<MetricSet> metricSets) {
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(configuration.getOutputFile())))) {
            writeHeaderRow(metricSets, printWriter);
            metricSets.forEach(metrics -> writeTestCaseRow(metrics, printWriter));
        }
        log.info("Wrote output to {}", configuration.getOutputFile());
    }

    private void writeHeaderRow(Collection<MetricSet> metricSets, PrintWriter printWriter) {
        List<String> list = new LinkedList<>();
        list.add("TestCase");
        list.addAll(metricSets.stream()
            .findFirst()
            .map(MetricSet::getMetrics)
            .map(metrics -> metrics
                .stream()
                .map(Metric::getIdentifier)
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList()));
        printWriter.println(String.join(",", list));
    }

    @SneakyThrows
    private void writeTestCaseRow(MetricSet metricSet, PrintWriter printWriter) {
        List<String> list = new LinkedList<>();
        list.add(metricSet.getTestCase()
            .getFullyQualifiedMethodName()
            .replace(',', ';')
            .replace('\n', ';'));
        list.addAll(metricSet.getMetrics()
            .stream()
            .map(Metric::getStringValue)
            .collect(Collectors.toList()));
        printWriter.println(String.join(",", list));
    }
}
