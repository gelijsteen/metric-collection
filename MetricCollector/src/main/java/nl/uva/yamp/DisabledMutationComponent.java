package nl.uva.yamp;

import dagger.BindsInstance;
import dagger.Component;
import nl.uva.yamp.collector.callgraph.javassist.JavassistCallGraphModule;
import nl.uva.yamp.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.yamp.collector.module.maven.MavenTargetModule;
import nl.uva.yamp.collector.mutation.disabled.DisabledMutationModule;
import nl.uva.yamp.core.CoreModule;
import nl.uva.yamp.writer.csv.CsvWriterModule;

import java.nio.file.Path;

@Component(modules = {
    CoreModule.class,
    MavenTargetModule.class,
    JacocoCoverageModule.class,
    JavassistCallGraphModule.class,
    DisabledMutationModule.class,
    CsvWriterModule.class
})
public interface DisabledMutationComponent {

    Application application();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder projectDirectory(Path projectDirectory);

        DisabledMutationComponent build();
    }
}