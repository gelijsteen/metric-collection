package nl.uva.yamp.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Set;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class CombinedData {

    @NonNull
    private final TestCase testCase;
    @NonNull
    private final Set<Constructor> constructors;
    @NonNull
    private final Set<Method> methods;
    @NonNull
    private final Integer mutationScore;
}
