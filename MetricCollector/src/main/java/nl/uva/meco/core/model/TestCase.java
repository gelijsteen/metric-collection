package nl.uva.meco.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class TestCase {

    @NonNull
    private final String packageName;
    @NonNull
    private final String className;
    @NonNull
    private final String methodName;
    private final String identifier; // Used to distinguish between parameterized tests.

    public String getFullyQualifiedClassName() {
        return packageName + "." + className;
    }

    public String getFullyQualifiedMethodName() {
        return packageName + "." + className + "." + methodName + Optional.ofNullable(identifier).orElse("");
    }
}
