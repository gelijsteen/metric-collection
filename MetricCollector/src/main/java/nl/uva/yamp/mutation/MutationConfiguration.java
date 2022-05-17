package nl.uva.yamp.mutation;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import nl.uva.yamp.mutation.pitest.PitestMutationConfiguration;

@Getter
@Setter
public class MutationConfiguration {

    @NonNull
    private NestedMutationConfiguration mutation;

    @Getter
    @Setter
    public static class NestedMutationConfiguration {

        private PitestMutationConfiguration pitest;
    }
}
