package nl.uva.yamp.core.writer;

import nl.uva.yamp.core.model.MetricSet;

import java.util.Collection;

public interface Writer {

    void write(Collection<MetricSet> metricSets);
}
