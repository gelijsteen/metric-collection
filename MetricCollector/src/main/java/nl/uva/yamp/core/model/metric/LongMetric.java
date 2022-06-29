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
public class LongMetric implements Metric {

    @NonNull
    private final String identifier;
    @NonNull
    private final Long value;

    @Override
    public String getStringValue() {
        return Long.toString(value);
    }
}
