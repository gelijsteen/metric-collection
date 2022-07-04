package nl.uva.meco.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.With;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Method {

    @NonNull
    private final String packageName;
    @NonNull
    private final String className;
    @NonNull
    private final String methodName;
    @NonNull
    private final String descriptor; // Used to distinguish between overloaded methods.
    @NonNull
    private final Integer loc;
    @With
    @NonNull
    private final Boolean direct;

    public String getFullyQualifiedClassName() {
        return packageName + "." + className;
    }

    public String getFullyQualifiedMethodName() {
        return getFullyQualifiedClassName() + "." + methodName;
    }

    public String getSignature() {
        return getFullyQualifiedMethodName() + " " + descriptor;
    }
}
