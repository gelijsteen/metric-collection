package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CoverageConstructor extends Constructor {

    @NonNull
    private final Integer loc;

    @Builder
    private CoverageConstructor(String packageName, String className, String descriptor, @NonNull Integer loc) {
        super(packageName, className, descriptor);
        this.loc = loc;
    }
}
