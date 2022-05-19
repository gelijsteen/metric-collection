package nl.uva.yamp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.MetricCalculation;

import javax.inject.Inject;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Application {

    public static void main(String[] args) {
        if (args.length != 2) {
            log.info("Usage: <configurationFile.yml> <projectDirectory>");
            return;
        }

        createApplication(args).run();
    }

    private static Application createApplication(String[] args) {
        return DaggerApplicationComponent.builder()
            .configurationFile(Paths.get(args[0]))
            .projectDirectory(Paths.get(args[1]))
            .build()
            .application();
    }

    private final MetricCalculation metricCalculation;

    private void run() {
        metricCalculation.calculate();
    }
}
