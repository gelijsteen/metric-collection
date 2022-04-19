package nl.uva.yamp.core.filter;

import lombok.NonNull;
import nl.uva.yamp.core.model.Coverage;

@FunctionalInterface
public interface CoverageFilter {

    Coverage apply(Coverage coverage);

    static CoverageFilter identity() {
        return coverage -> coverage;
    }

    default CoverageFilter andThen(@NonNull CoverageFilter after) {
        return coverage -> after.apply(this.apply(coverage));
    }
}
