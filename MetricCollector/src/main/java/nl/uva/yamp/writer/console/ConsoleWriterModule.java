package nl.uva.yamp.writer.console;

import dagger.Binds;
import dagger.Module;
import nl.uva.yamp.core.writer.Writer;

@Module
public interface ConsoleWriterModule {

    @Binds
    Writer writer(ConsoleWriter impl);
}
