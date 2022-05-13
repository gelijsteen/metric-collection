package nl.uva.yamp.reader.jacoco;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class JacocoReaderConfiguration {

    @NonNull
    private String projectDirectory;
    @NonNull
    private Boolean parallel;
}
