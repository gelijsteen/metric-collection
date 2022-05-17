package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.model.CombinedData;
import nl.uva.yamp.core.model.metric.Metric;

public interface MetricCollector {

    Metric collect(CombinedData combinedData);
}
