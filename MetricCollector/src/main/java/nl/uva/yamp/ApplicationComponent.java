package nl.uva.yamp;

import dagger.BindsInstance;
import dagger.Component;

import java.nio.file.Path;

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder projectDirectory(Path projectDirectory);

        ApplicationComponent build();
    }
}
