package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import nl.uva.yamp.core.model.metric.Metric;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class MetricSet {

    @NonNull
    private final TestCase testCase;
    @NonNull
    private final List<Metric> metrics;
}
