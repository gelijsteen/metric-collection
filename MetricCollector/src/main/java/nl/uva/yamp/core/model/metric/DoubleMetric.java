package nl.uva.yamp.core.model.metric;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.text.DecimalFormat;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DoubleMetric implements Metric {

    @NonNull
    private final String identifier;
    @NonNull
    private final Double value;

    @Override
    public String getStringValue() {
        return new DecimalFormat("0.00").format(value);
    }
}
