package nl.uva.meco.writer.console;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.meco.core.model.MetricSet;
import nl.uva.meco.core.writer.Writer;

import javax.inject.Inject;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(onConstructor = @__(@Inject))
class ConsoleWriter implements Writer {

    @Override
    public void write(Collection<MetricSet> metricSets) {
        metricSets.forEach(this::writeRow);
    }

    private void writeRow(MetricSet metricSet) {
        List<String> list = new LinkedList<>();
        list.add(metricSet.getTestCase().getFullyQualifiedMethodName());
        list.addAll(metricSet.getMetrics()
            .stream()
            .map(metric -> metric.getIdentifier() + "=" + metric.getStringValue())
            .collect(Collectors.toList()));
        log.info(String.join(", ", list));
    }
}
