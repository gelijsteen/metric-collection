package nl.uva.yamp.reader;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import nl.uva.yamp.reader.jacoco.JacocoReaderConfiguration;

@Getter
@Setter
public class ReaderConfiguration {

    @NonNull
    private NestedReaderConfiguration reader;

    @Getter
    @Setter
    public static class NestedReaderConfiguration {

        private JacocoReaderConfiguration jacoco;
    }
}
