package nl.uva.yamp.collector.mutation.pitest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.model.TargetDirectory;
import nl.uva.yamp.util.PathResolver;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class PitestMutationCollector implements MutationCollector {

    private static final String PITEST_RESULT_FILE = "mutations.csv";
    private static final String PITEST_TEMPLATE_FILE = "pitest-template.xml";
    private static final String S_PARAM = "<param>";
    private static final String E_PARAM = "</param>";

    private final MavenProfileAppender mavenProfileAppender;

    @SneakyThrows
    public DataSet collect(TargetDirectory targetDirectory, DataSet dataSet) {
        log.debug("Calculating mutation score for: {}", dataSet.getTestCase().getFullyQualifiedMethodName());
        Path pomFile = Files.createTempFile(targetDirectory.getPath().getParent(), "temp-", ".xml");
        Path reportDirectory = Files.createTempDirectory(targetDirectory.getPath().getParent(), "temp-");
        try {
            addPitestProfileToPomFile(targetDirectory, dataSet, pomFile, reportDirectory);

            invokePitestProfile(pomFile);

            Set<Mutation> mutations = collectMutations(reportDirectory);

            return dataSet.withMutations(mutations);
        } finally {
            Files.deleteIfExists(pomFile);
            Files.deleteIfExists(reportDirectory.resolve(PITEST_RESULT_FILE));
            Files.deleteIfExists(reportDirectory);
        }
    }

    @SneakyThrows
    private void addPitestProfileToPomFile(TargetDirectory targetDirectory, DataSet dataSet, Path pomFile, Path reportDirectory) {
        String originalPom = Files.readString(targetDirectory.getPath().getParent().resolve("pom.xml"));
        String profileTemplate = Files.readString(PathResolver.getPath(PITEST_TEMPLATE_FILE));

        String pitestProfile = replaceTemplatePlaceholders(dataSet, reportDirectory, profileTemplate);

        Files.writeString(pomFile, mavenProfileAppender.addProfileToPomFile(originalPom, pitestProfile));
    }

    @NotNull
    private String replaceTemplatePlaceholders(DataSet dataSet, Path reportDirectory, String profileTemplate) {
        StringBuilder targetClasses = dataSet.getMethods().stream()
            .map(Method::getFullyQualifiedClassName)
            .distinct()
            .reduce(new StringBuilder(), (stringBuilder, fqn) -> stringBuilder.append(S_PARAM).append(fqn).append(E_PARAM), StringBuilder::append);

        return profileTemplate.replace("${targetClasses}", targetClasses)
            .replace("${targetTests}", S_PARAM + dataSet.getTestCase().getFullyQualifiedClassName() + E_PARAM)
            .replace("${includedTestMethods}", S_PARAM + dataSet.getTestCase().getMethodName() + E_PARAM)
            .replace("${reportsDirectory}", reportDirectory.subpath(reportDirectory.getNameCount() - 1, reportDirectory.getNameCount()).toString());
    }

    @SneakyThrows
    private void invokePitestProfile(Path pomFile) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("mvn", "-f", pomFile.toFile().toString(), "-Ppitest-analysis", "org.pitest:pitest-maven:mutationCoverage");
        Process process = processBuilder.start();

        OutputHandler errorOutput = new OutputHandler(process.getErrorStream());
        Executors.newSingleThreadExecutor().submit(errorOutput);

        if (process.waitFor() != 0) {
            errorOutput.getOutput().forEach(log::warn);
            throw new IllegalStateException("Maven build failed.");
        }
    }

    @SneakyThrows
    private Set<Mutation> collectMutations(Path reportDirectory) {
        Set<Mutation> result = new HashSet<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(reportDirectory.resolve(PITEST_RESULT_FILE))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");

                // Filter for valid mutation results.
                if (Set.of("KILLED", "SURVIVED").contains(parts[5])) {
                    result.add(Mutation.builder()
                        .fullyQualifiedMethodName(parts[1] + "." + parts[3])
                        .mutationOperator(parts[2])
                        .lineNumber(Integer.parseInt(parts[4]))
                        .killed("KILLED".equals(parts[5]))
                        .disjoint(false)
                        .build());
                }
            }
        }
        return result;
    }

    @Getter
    @RequiredArgsConstructor
    private static class OutputHandler implements Runnable {

        private final InputStream inputStream;
        private final List<String> output = new LinkedList<>();

        @Override
        @SneakyThrows
        public void run() {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                bufferedReader.lines().forEach(output::add);
            }
        }
    }
}
