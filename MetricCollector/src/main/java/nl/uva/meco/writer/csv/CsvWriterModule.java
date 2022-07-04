package nl.uva.meco.writer.csv;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import nl.uva.meco.core.writer.Writer;
import nl.uva.meco.util.ConfigurationLoader;
import nl.uva.meco.writer.WriterConfiguration;
import nl.uva.meco.writer.WriterConfiguration.CsvWriterConfiguration;

import java.util.Optional;

@Module
public interface CsvWriterModule {

    @Binds
    Writer writer(CsvWriter impl);

    @Provides
    static CsvWriterConfiguration csvWriterConfiguration() {
        WriterConfiguration writerConfiguration = ConfigurationLoader.loadConfiguration(WriterConfiguration.class);
        return Optional.ofNullable(writerConfiguration)
            .map(WriterConfiguration::getWriter)
            .map(WriterConfiguration.NestedWriterConfiguration::getCsv)
            .orElseThrow();
    }
}
