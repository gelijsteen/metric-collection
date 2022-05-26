package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.With;

import java.util.Set;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Coverage {

    @NonNull
    private final TestCase testCase;
    @With
    @NonNull
    private final Integer mutationScore;
    @NonNull
    private final Set<Constructor> constructors;
    @NonNull
    private final Set<Method> methods;
    @NonNull
    private final Set<Constructor> testConstructors;
    @NonNull
    private final Set<Method> testMethods;
}
