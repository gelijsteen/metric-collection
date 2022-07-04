package nl.uva.meco.writer.console;

import dagger.Binds;
import dagger.Module;
import nl.uva.meco.core.writer.Writer;

@Module
public interface ConsoleWriterModule {

    @Binds
    Writer writer(ConsoleWriter impl);
}
