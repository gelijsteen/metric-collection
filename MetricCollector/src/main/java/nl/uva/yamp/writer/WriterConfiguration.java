package nl.uva.yamp.writer;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import nl.uva.yamp.writer.csv.CsvWriterConfiguration;

@Getter
@Setter
public class WriterConfiguration {

    @NonNull
    private NestedWriterConfiguration writer;

    @Getter
    @Setter
    public static class NestedWriterConfiguration {

        private CsvWriterConfiguration csv;
        private Object console;
    }
}
