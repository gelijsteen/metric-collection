package nl.uva.yamp.core.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class Constructor {

    @NonNull
    private final String packageName;
    @NonNull
    private final String className;
    @NonNull
    private final String descriptor; // Used to distinguish between overloaded methods.

    public String getSignature() {
        return packageName + "." + className + " " + descriptor;
    }
}
