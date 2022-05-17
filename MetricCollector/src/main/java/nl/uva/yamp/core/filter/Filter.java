package nl.uva.yamp.core.filter;

import lombok.NonNull;
import nl.uva.yamp.core.model.CombinedData;

@FunctionalInterface
public interface Filter {

    CombinedData apply(CombinedData combinedData);

    static Filter identity() {
        return combinedData -> combinedData;
    }

    default Filter andThen(@NonNull Filter after) {
        return combinedData -> after.apply(this.apply(combinedData));
    }
}
