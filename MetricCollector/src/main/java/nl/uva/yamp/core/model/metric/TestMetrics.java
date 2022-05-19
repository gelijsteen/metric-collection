package nl.uva.yamp.core.model.metric;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import nl.uva.yamp.core.model.TestCase;

import java.util.List;

@Getter
@Builder
@ToString
public class TestMetrics {

    @NonNull
    private final TestCase testCase;
    @NonNull
    private final List<Metric> metrics;
}
