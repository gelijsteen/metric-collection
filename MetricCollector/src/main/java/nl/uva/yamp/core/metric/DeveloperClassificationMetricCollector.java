package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.metric.Metric;
import nl.uva.yamp.core.model.metric.StringMetric;

import javax.inject.Inject;
import java.util.Set;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DeveloperClassificationMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return StringMetric.builder()
            .identifier("DEV")
            .value(calculate(dataSet))
            .build();
    }

    private String calculate(DataSet dataSet) {
        String className = dataSet.getTestCase().getClassName();
        Set<String> integrationTests = Set.of("IT", "Integration", "integration");
        Set<String> unitTests = Set.of("Test", "test");

        if (integrationTests.stream().anyMatch(className::contains)) {
            return "IT";
        }
        if (unitTests.stream().anyMatch(className::endsWith)) {
            return "UT";
        }
        return "NA";
    }
}
