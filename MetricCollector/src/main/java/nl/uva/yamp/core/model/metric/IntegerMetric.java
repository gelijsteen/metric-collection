package nl.uva.yamp.core.model.metric;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@Builder
@ToString
public class IntegerMetric implements Metric {

    @NonNull
    private final String identifier;
    @NonNull
    private final int value;

    @Override
    public String getStringValue() {
        return Integer.toString(value);
    }
}
