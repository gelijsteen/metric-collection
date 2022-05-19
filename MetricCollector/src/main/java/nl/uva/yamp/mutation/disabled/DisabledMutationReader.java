package nl.uva.yamp.mutation.disabled;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.mutation.MutationReader;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DisabledMutationReader implements MutationReader {

    @Override
    public Mutation read(Coverage coverage) {
        return Mutation.builder()
            .testMethod(coverage.getTestMethod())
            .mutationScore(0)
            .build();
    }
}
