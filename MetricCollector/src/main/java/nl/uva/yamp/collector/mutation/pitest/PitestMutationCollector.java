package nl.uva.yamp.collector.mutation.pitest;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.Mutation;
import nl.uva.yamp.core.collector.MutationCollector;
import nl.uva.yamp.util.PathResolver;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PitestMutationCollector implements MutationCollector {

    private static final Pattern PATTERN = Pattern.compile(".* Test strength (\\d+)%");
    private final AtomicInteger mutationScore = new AtomicInteger(0);
    private final Path projectDirectory;

    @SneakyThrows
    public Mutation collect(Coverage coverage) {
        log.debug("Calculating mutation score for: {}", coverage.getTestCase().getFullyQualifiedMethodName());
        Path pomFile = Files.createTempFile(projectDirectory, "yamp-", ".xml");
        Path reportDirectory = Files.createTempDirectory(projectDirectory, "yamp-");
        try {
            Path originalPom = projectDirectory.resolve("pom.xml");

            try (BufferedReader bufferedReader = Files.newBufferedReader(originalPom); BufferedWriter bufferedWriter = Files.newBufferedWriter(pomFile)) {
                bufferedReader.transferTo(bufferedWriter);
            }

            MavenProfileAppender processor = new MavenProfileAppender();
            processor.append(pomFile, PathResolver.getPath("pitest-template.xml"));

            StringBuilder classCoverage = coverage.getMethods().stream()
                .map(Method::getFullyQualifiedClassName)
                .distinct()
                .reduce(new StringBuilder(), (stringBuilder, fqn) -> stringBuilder.append("<param>").append(fqn).append("</param>"), StringBuilder::append);

            String pomContents = Files.readString(pomFile);
            Files.writeString(pomFile, pomContents
                .replace("${targetClasses}", classCoverage)
                .replace("${targetTests}", "<param>" + coverage.getTestCase().getFullyQualifiedClassName() + "</param>")
                .replace("${includedTestMethods}", "<param>" + coverage.getTestCase().getMethodName() + "</param>")
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
                .testCase(coverage.getTestCase())
                .mutationScore(mutationScore.get())
                .build();
        } finally {
            Files.deleteIfExists(pomFile);
            Files.deleteIfExists(reportDirectory.resolve("mutations.csv"));
            Files.deleteIfExists(reportDirectory);
        }
    }
}
