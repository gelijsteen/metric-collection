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
@EqualsAndHashCode(exclude = "killed")
public class Mutation {

    @NonNull
    private final String fullyQualifiedMethodName;
    @NonNull
    private final String mutationOperator;
    @NonNull
    private final Integer lineNumber;
    @NonNull
    private final Boolean killed;
    @With
    @NonNull
    private final Boolean disjoint;
}
