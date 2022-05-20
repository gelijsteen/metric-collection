package nl.uva.yamp.writer.csv;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import nl.uva.yamp.core.writer.Writer;
import nl.uva.yamp.util.ConfigurationLoader;
import nl.uva.yamp.writer.WriterConfiguration;

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
