package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString
public class CallGraphMethod extends Method {

    @NonNull
    private final Set<CallGraphConstructor> constructors;
    @NonNull
    private final Set<CallGraphMethod> methods;

    @Builder
    public CallGraphMethod(String packageName, String className, String methodName,
                           @NonNull Set<CallGraphConstructor> constructors,
                           @NonNull Set<CallGraphMethod> methods) {
        super(packageName, className, methodName);
        this.constructors = constructors;
        this.methods = methods;
    }
}
