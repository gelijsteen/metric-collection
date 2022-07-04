package nl.uva.meco.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.Method;
import nl.uva.meco.core.model.metric.IntegerMetric;
import nl.uva.meco.core.model.metric.Metric;

import javax.inject.Inject;
import java.util.stream.Collectors;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class IndirectPackagesCoveredMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("IPC")
            .value(calculate(dataSet))
            .build();
    }

    private int calculate(DataSet dataSet) {
        return dataSet.getMethods()
            .stream()
            .map(Method::getPackageName)
            .collect(Collectors.toSet())
            .size();
    }
}
