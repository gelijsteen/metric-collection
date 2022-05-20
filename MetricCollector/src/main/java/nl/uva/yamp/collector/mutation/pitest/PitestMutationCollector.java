package nl.uva.yamp.collector.mutation.pitest;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.util.PathResolver;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class PitestMutationCollector implements MutationCollector {

    private static final Pattern PATTERN = Pattern.compile(".* Test strength (\\d+)%");
    private static final String PITEST_RESULT_FILE = "mutations.csv";
    private static final String PITEST_TEMPLATE_FILE = "pitest-template.xml";
    private final Path projectDirectory;
    private final MavenProfileAppender mavenProfileAppender;

    @SneakyThrows
    public Mutation collect(Coverage coverage) {
        log.debug("Calculating mutation score for: {}", coverage.getTestCase().getFullyQualifiedMethodName());
        Path pomFile = Files.createTempFile(projectDirectory, "temp-", ".xml");
        Path reportDirectory = Files.createTempDirectory(projectDirectory, "temp-");
        try {
            addPitestProfileToPomFile(coverage, pomFile, reportDirectory);

            int mutationScore = invokePitestProfile(pomFile);

            return Mutation.builder()
                .testCase(coverage.getTestCase())
                .mutationScore(mutationScore)
                .build();
        } finally {
            Files.deleteIfExists(pomFile);
            Files.deleteIfExists(reportDirectory.resolve(PITEST_RESULT_FILE));
            Files.deleteIfExists(reportDirectory);
        }
    }

    @SneakyThrows
    private void addPitestProfileToPomFile(Coverage coverage, Path pomFile, Path reportDirectory) {
        String originalPom = Files.readString(projectDirectory.resolve("pom.xml"));
        String profileTemplate = Files.readString(PathResolver.getPath(PITEST_TEMPLATE_FILE));

        String pitestProfile = replaceTemplatePlaceholders(coverage, reportDirectory, profileTemplate);

        Files.writeString(pomFile, mavenProfileAppender.addProfileToPomFile(originalPom, pitestProfile));
    }

    @NotNull
    private String replaceTemplatePlaceholders(Coverage coverage, Path reportDirectory, String profileTemplate) {
        StringBuilder targetClasses = coverage.getMethods().stream()
            .map(Method::getFullyQualifiedClassName)
            .distinct()
            .reduce(new StringBuilder(), (stringBuilder, fqn) -> stringBuilder.append("<param>").append(fqn).append("</param>"), StringBuilder::append);

        return profileTemplate.replace("${targetClasses}", targetClasses)
            .replace("${targetTests}", "<param>" + coverage.getTestCase().getFullyQualifiedClassName() + "</param>")
            .replace("${includedTestMethods}", "<param>" + coverage.getTestCase().getMethodName() + "</param>")
            .replace("${reportsDirectory}", reportDirectory.subpath(reportDirectory.getNameCount() - 1, reportDirectory.getNameCount()).toString());
    }

    @SneakyThrows
    private int invokePitestProfile(Path pomFile) {
        AtomicInteger mutationScore = new AtomicInteger(0);

        InvocationRequest request = generateMavenRequest(pomFile, mutationScore);

        Invoker invoker = new DefaultInvoker();
        InvocationResult result = invoker.execute(request);

        if (result.getExitCode() != 0) {
            throw new IllegalStateException("Maven build failed.");
        }

        return mutationScore.get();
    }

    private InvocationRequest generateMavenRequest(Path pomFile, AtomicInteger mutationScore) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(pomFile.toFile());
        request.setGoals(List.of("-Ppitest-analysis", "org.pitest:pitest-maven:mutationCoverage"));
        request.setInputStream(InputStream.nullInputStream());
        request.setOutputHandler(line -> matchMutationScore(mutationScore, line));
        request.setErrorHandler(line -> {
            // Empty to suppress warning/error logging.
        });
        return request;
    }

    private void matchMutationScore(AtomicInteger mutationScore, String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
            String group = matcher.group(1);
            mutationScore.set(Integer.parseInt(group));
        }
    }
}
