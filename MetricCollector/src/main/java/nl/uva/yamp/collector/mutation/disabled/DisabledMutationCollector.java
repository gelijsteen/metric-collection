package nl.uva.yamp.collector.mutation.disabled;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Mutation;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DisabledMutationCollector implements MutationCollector {

    @Override
    public Mutation collect(Coverage coverage) {
        return Mutation.builder()
            .testMethod(coverage.getTestMethod())
            .mutationScore(0)
            .build();
    }
}
