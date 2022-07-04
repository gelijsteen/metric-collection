package nl.uva.meco.core.metric;

import nl.uva.meco.core.model.DataSet;
import nl.uva.meco.core.model.metric.Metric;

public interface MetricCollector {

    Metric collect(DataSet dataSet);
}
