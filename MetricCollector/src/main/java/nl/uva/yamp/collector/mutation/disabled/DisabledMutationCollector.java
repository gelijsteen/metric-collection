package nl.uva.yamp.collector.mutation.disabled;

import lombok.NoArgsConstructor;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.model.Coverage;

import javax.inject.Inject;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class DisabledMutationCollector implements MutationCollector {

    @Override
    public Coverage collect(Coverage coverage) {
        return Coverage.builder()
            .testCase(coverage.getTestCase())
            .mutationScore(0)
            .constructors(coverage.getConstructors())
            .methods(coverage.getMethods())
            .testConstructors(coverage.getTestConstructors())
            .testMethods(coverage.getTestMethods())
            .build();
    }
}
