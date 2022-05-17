package nl.uva.yamp.coverage.jacoco;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class JacocoCoverageConfiguration {

    @NonNull
    private String projectDirectory;
    @NonNull
    private Boolean parallel;
}
