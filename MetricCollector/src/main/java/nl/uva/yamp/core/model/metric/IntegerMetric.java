package nl.uva.yamp.core.model.metric;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class IntegerMetric implements Metric {

    @NonNull
    private final String identifier;
    @NonNull
    private final Integer value;

    @Override
    public String getStringValue() {
        return Integer.toString(value);
    }
}
