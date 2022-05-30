package nl.uva.yamp.writer;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class WriterConfiguration {

    @NonNull
    private NestedWriterConfiguration writer;

    @Getter
    @Setter
    public static class NestedWriterConfiguration {

        private CsvWriterConfiguration csv;
    }

    @Getter
    @Setter
    public static class CsvWriterConfiguration {

        @NonNull
        private String outputFile;
    }
}
