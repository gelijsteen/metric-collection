package nl.uva.yamp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.MetricCalculation;

import javax.inject.Inject;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Application {

    public static void main(String[] args) {
        if (args.length != 1) {
            log.info("Usage: <projectDirectory>");
            System.exit(1);
        }

        Path projectDirectory = Paths.get(args[0]);
        if (!projectDirectory.toFile().exists()) {
            log.warn("Project directory [{}] does not exist.", projectDirectory);
            System.exit(2);
        }

        createApplication(projectDirectory).run();
    }

    private static Application createApplication(Path projectDirectory) {
        return DaggerApplicationComponent.builder()
            .projectDirectory(projectDirectory)
            .build()
            .application();
    }

    private final MetricCalculation metricCalculation;

    void run() {
        metricCalculation.calculate();
    }
}
