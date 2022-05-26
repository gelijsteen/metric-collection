package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DistinctPackageHierarchiesCoveredMetricCollector implements MetricCollector {

    @Override
    public Metric collect(DataSet dataSet) {
        return IntegerMetric.builder()
            .identifier("DPHC")
            .value(calculate(dataSet))
            .build();
    }

    private int calculate(DataSet dataSet) {
        Set<String> hierarchyPackages = new HashSet<>();
        dataSet.getMethods()
            .stream()
            .map(Method::getPackageName)
            .sorted()
            .forEach(sortedPackage -> {
                if (hierarchyPackages.stream().noneMatch(sortedPackage::contains)) {
                    hierarchyPackages.add(sortedPackage);
                }
            });
        return hierarchyPackages.size();
    }
}
