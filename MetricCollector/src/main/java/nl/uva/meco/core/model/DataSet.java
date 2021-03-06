package nl.uva.meco.core.model;

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
public class DataSet {

    @NonNull
    private final TestCase testCase;
    @NonNull
    private final Set<Constructor> constructors;
    @NonNull
    private final Set<Method> methods;
    @NonNull
    private final Set<Constructor> testConstructors;
    @NonNull
    private final Set<Method> testMethods;
    @With
    @NonNull
    private final Set<Mutation> mutations;
}
