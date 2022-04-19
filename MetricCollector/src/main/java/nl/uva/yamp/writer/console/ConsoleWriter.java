package nl.uva.yamp.writer.console;

import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.Writer;
import nl.uva.yamp.core.model.metric.TestMetrics;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ConsoleWriter implements Writer {

    @Override
    public void write(Collection<TestMetrics> testMetrics) {
        testMetrics.forEach(this::writeRow);
    }

    private void writeRow(TestMetrics testMetrics) {
        List<String> list = new LinkedList<>();
        list.add(testMetrics.getTestMethod().getFullyQualifiedMethodName());
        list.addAll(testMetrics.getMetrics()
            .stream()
            .map(metric -> metric.getIdentifier() + "=" + metric.getStringValue())
            .collect(Collectors.toList()));
        log.info(String.join(", ", list));
    }
}
