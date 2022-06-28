package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DeveloperClassificationMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("UT")
            .value(isUnitTest(dataSet) ? 1 : 0)
            .build();
    }

    private boolean isUnitTest(DataSet dataSet) {
        String className = dataSet.getTestCase().getClassName();
        return dataSet.getMethods().stream()
            .map(Method::getClassName)
            .filter(className::contains)
            .anyMatch(clazz -> isTestClassMatch(className, clazz));
    }

    private boolean isTestClassMatch(String className, String clazz) {
        return className.equalsIgnoreCase(clazz + "Test") ||
            className.equalsIgnoreCase("Test" + clazz);
    }
}
