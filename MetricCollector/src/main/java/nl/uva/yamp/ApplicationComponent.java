package nl.uva.yamp;

import dagger.BindsInstance;
import dagger.Component;
import nl.uva.yamp.collector.callgraph.javassist.JavassistCallGraphModule;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.yamp.collector.mutation.pitest.PitestMutationModule;
import nl.uva.yamp.writer.console.ConsoleWriterModule;

import java.nio.file.Path;

@Component(modules = {
    ApplicationModule.class,
    JacocoCoverageModule.class,
    JavassistCallGraphModule.class,
    PitestMutationModule.class,
    ConsoleWriterModule.class
})
public interface ApplicationComponent {

    Application application();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder projectDirectory(Path projectDirectory);

        ApplicationComponent build();
    }
}
