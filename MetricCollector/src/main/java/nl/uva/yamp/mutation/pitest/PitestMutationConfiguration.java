package nl.uva.yamp.mutation.pitest;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PitestMutationConfiguration {

    @NonNull
    private String projectDirectory;
}
