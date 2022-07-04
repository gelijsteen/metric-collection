package nl.uva.meco;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.meco.core.MetricCalculation;

import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Application {

    public static void main(String[] args) {
        if (args.length < 1 || args.length > 2) {
            log.info("Usage: <projectDirectory> <disable mutation testing (-D)>");
            System.exit(1);
        }

        Path projectDirectory = Paths.get(args[0]);
        if (!projectDirectory.toFile().exists()) {
            log.warn("Project directory [{}] does not exist.", projectDirectory);
            System.exit(2);
        }

        if (isMutationTestingDisabled(args)) {
            createApplicationWithoutMutationTests(projectDirectory).run();
        } else {
            createApplication(projectDirectory).run();
        }
    }

    private static boolean isMutationTestingDisabled(String[] args) {
        return args.length == 2 && args[1].equals("-D");
    }

    private static Application createApplication(Path projectDirectory) {
        return DaggerApplicationComponent.builder()
            .projectDirectory(projectDirectory)
            .build()
            .application();
    }

    private static Application createApplicationWithoutMutationTests(Path projectDirectory) {
        return DaggerDisabledMutationComponent.builder()
            .projectDirectory(projectDirectory)
            .build()
            .application();
    }

    private final MetricCalculation metricCalculation;

    void run() {
        metricCalculation.calculate();
    }
}
