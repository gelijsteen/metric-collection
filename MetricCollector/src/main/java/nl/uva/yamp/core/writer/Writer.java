package nl.uva.yamp.core.writer;

import nl.uva.yamp.core.model.metric.TestMetrics;

import java.util.Collection;

public interface Writer {

    void write(Collection<TestMetrics> testMetrics);
}
