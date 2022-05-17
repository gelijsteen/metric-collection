package nl.uva.yamp.coverage;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import nl.uva.yamp.coverage.jacoco.JacocoCoverageConfiguration;

@Getter
@Setter
public class CoverageConfiguration {

    @NonNull
    private NestedCoverageConfiguration coverage;

    @Getter
    @Setter
    public static class NestedCoverageConfiguration {

        private JacocoCoverageConfiguration jacoco;
    }
}
