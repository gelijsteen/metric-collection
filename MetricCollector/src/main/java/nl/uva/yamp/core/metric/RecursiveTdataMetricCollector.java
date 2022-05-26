package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class RecursiveTdataMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("rTDATA")
            .value(calculate(dataSet))
            .build();
    }

    private int calculate(DataSet dataSet) {
        return dataSet.getConstructors()
            .stream()
            .filter(Constructor::getDirect)
            .map(Constructor::getSignature)
            .collect(Collectors.toSet())
            .size();
    }
}
