package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

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

    public String getFullyQualifiedClassName() {
        return packageName + "." + className;
    }

    public String getFullyQualifiedMethodName() {
        return packageName + "." + className + "." + methodName;
    }
}
