package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CombinedConstructor extends Constructor {

    @NonNull
    private final Set<CombinedConstructor> constructors;
    @NonNull
    private final Set<CombinedMethod> methods;
    @NonNull
    private final Integer loc;

    @Builder
    public CombinedConstructor(String packageName, String className, String descriptor,
                               @NonNull Set<CombinedConstructor> constructors,
                               @NonNull Set<CombinedMethod> methods,
                               @NonNull Integer loc) {
        super(packageName, className, descriptor);
        this.constructors = constructors;
        this.methods = methods;
        this.loc = loc;
    }
}
