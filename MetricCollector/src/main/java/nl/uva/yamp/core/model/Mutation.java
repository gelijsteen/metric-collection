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
public class Mutation {

    @NonNull
    private final TestCase testCase;
    @NonNull
    private final Integer mutationScore;
}
