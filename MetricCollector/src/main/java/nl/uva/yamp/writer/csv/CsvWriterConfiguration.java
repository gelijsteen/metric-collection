package nl.uva.yamp.writer.csv;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CsvWriterConfiguration {

    @NonNull
    private String outputFile;
}
