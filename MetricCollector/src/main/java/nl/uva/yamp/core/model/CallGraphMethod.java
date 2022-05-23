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
public class CallGraphMethod extends Method {

    @NonNull
    private final Set<CallGraphConstructor> constructors;
    @NonNull
    private final Set<CallGraphMethod> methods;

    @Builder
    public CallGraphMethod(String packageName, String className, String methodName, String descriptor,
                           @NonNull Set<CallGraphConstructor> constructors,
                           @NonNull Set<CallGraphMethod> methods) {
        super(packageName, className, methodName, descriptor);
        this.constructors = constructors;
        this.methods = methods;
    }
}
