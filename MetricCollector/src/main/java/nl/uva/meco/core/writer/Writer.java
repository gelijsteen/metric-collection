package nl.uva.meco.core.writer;

import nl.uva.meco.core.model.MetricSet;

import java.util.Collection;

public interface Writer {

    void write(Collection<MetricSet> metricSets);
}
