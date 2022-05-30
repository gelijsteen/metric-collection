package nl.uva.yamp.core.metric;

import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.metric.Metric;

public interface MetricCollector {

    Metric collect(DataSet dataSet);
}
