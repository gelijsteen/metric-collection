package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CoverageMethod extends Method {

    @NonNull
    private final Integer loc;

    @Builder
    private CoverageMethod(String packageName, String className, String methodName, String descriptor, @NonNull Integer loc) {
        super(packageName, className, methodName, descriptor);
        this.loc = loc;
    }
}
