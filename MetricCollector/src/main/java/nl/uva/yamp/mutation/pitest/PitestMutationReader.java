package nl.uva.yamp.mutation.pitest;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.mutation.MutationReader;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class PitestMutationReader implements MutationReader {

    private static final Pattern PATTERN = Pattern.compile(".* Test strength (\\d+)%");
    private final AtomicInteger mutationScore = new AtomicInteger(0);
    private final PitestMutationConfiguration configuration;

    @SneakyThrows
    public Mutation read(Coverage coverage) {
        log.debug("Calculating mutation score for: {}", coverage.getTestMethod().getFullyQualifiedMethodName());
        Path rootPath = Paths.get(configuration.getProjectDirectory());
        Path pomFile = Files.createTempFile(rootPath, "yamp-", ".xml");
        Path reportDirectory = Files.createTempDirectory(rootPath, "yamp-");
        try {
            Path originalPom = Paths.get(configuration.getProjectDirectory(), "pom.xml");

            try (BufferedReader bufferedReader = Files.newBufferedReader(originalPom); BufferedWriter bufferedWriter = Files.newBufferedWriter(pomFile)) {
                bufferedReader.transferTo(bufferedWriter);
            }

            MavenProfileAppender processor = new MavenProfileAppender();
            processor.append(pomFile, Paths.get("MetricCollector/src/main/resources/pitest.xml"));

            StringBuilder classCoverage = coverage.getCoveredMethods().stream()
                .map(Method::getFullyQualifiedClassName)
                .distinct()
                .reduce(new StringBuilder(), (stringBuilder, fqn) -> stringBuilder.append("<param>").append(fqn).append("</param>"), StringBuilder::append);

            String pomContents = Files.readString(pomFile);
            Files.writeString(pomFile, pomContents
                .replace("${targetClasses}", classCoverage)
                .replace("${targetTests}", "<param>" + coverage.getTestMethod().getFullyQualifiedClassName() + "</param>")
                .replace("${includedTestMethods}", "<param>" + coverage.getTestMethod().getMethodName() + "</param>")
                .replace("${reportsDirectory}", reportDirectory.subpath(reportDirectory.getNameCount() - 1, reportDirectory.getNameCount()).toString()));

            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(pomFile.toFile());
            request.setGoals(List.of("-Ppitest-analysis", "org.pitest:pitest-maven:mutationCoverage"));
            request.setInputStream(InputStream.nullInputStream());
            request.setOutputHandler(line -> {
                Matcher matcher = PATTERN.matcher(line);
                if (matcher.find()) {
                    String group = matcher.group(1);
                    mutationScore.set(Integer.parseInt(group));
                }
            });
            request.setErrorHandler(line -> {
                // Empty to suppress warning/error logging.
            });

            Invoker invoker = new DefaultInvoker();
            InvocationResult result = invoker.execute(request);

            if (result.getExitCode() != 0) {
                throw new IllegalStateException("Maven build failed.");
            }

            return Mutation.builder()
                .testMethod(coverage.getTestMethod())
                .mutationScore(mutationScore.get())
                .build();
        } finally {
            Files.deleteIfExists(pomFile);
            Files.deleteIfExists(reportDirectory.resolve("mutations.csv"));
            Files.deleteIfExists(reportDirectory);
        }
    }
}
