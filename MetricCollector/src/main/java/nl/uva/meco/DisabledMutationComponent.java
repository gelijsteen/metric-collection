package nl.uva.meco;

import dagger.BindsInstance;
import dagger.Component;
import nl.uva.meco.collector.callgraph.javassist.JavassistCallGraphModule;
import nl.uva.meco.collector.coverage.jacoco.JacocoCoverageModule;
import nl.uva.meco.collector.module.maven.MavenTargetModule;
import nl.uva.meco.collector.mutation.disabled.DisabledMutationModule;
import nl.uva.meco.core.CoreModule;
import nl.uva.meco.writer.csv.CsvWriterModule;

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