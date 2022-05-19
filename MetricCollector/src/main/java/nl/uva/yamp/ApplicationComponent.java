package nl.uva.yamp;

import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Named;
import java.nio.file.Path;

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder configurationFile(@Named("configurationFile") Path configurationFile);

        @BindsInstance
        Builder projectDirectory(@Named("projectDirectory") Path projectDirectory);

        ApplicationComponent build();
    }
}
