package nl.uva.yamp.core.metric;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.CombinedMethod;
import nl.uva.yamp.core.model.metric.IntegerMetric;
import nl.uva.yamp.core.model.metric.Metric;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DistinctPackageHierarchiesCoveredMetricCollector implements MetricCollector {

    @Override
    public Metric collect(CombinedData combinedData) {
        return IntegerMetric.builder()
            .identifier("DPHC")
            .value(calculate(combinedData))
            .build();
    }

    private int calculate(CombinedData combinedData) {
        Set<String> hierarchyPackages = new HashSet<>();
        combinedData.getIndirectMethods()
            .stream()
            .map(CombinedMethod::getPackageName)
            .sorted()
            .forEach(sortedPackage -> {
                if (hierarchyPackages.stream().noneMatch(sortedPackage::contains)) {
                    hierarchyPackages.add(sortedPackage);
                }
            });
        return hierarchyPackages.size();
    }
}
