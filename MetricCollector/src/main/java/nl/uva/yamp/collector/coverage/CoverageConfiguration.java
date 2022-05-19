package nl.uva.yamp.collector.coverage;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageConfiguration;

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
