package nl.uva.yamp.collector.coverage.jacoco;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class JacocoCoverageConfiguration {

    @NonNull
    private Boolean parallel;
}
