package nl.uva.yamp.collector.coverage.jacoco;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.CoverageCollector;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.CoverageConstructor;
import nl.uva.yamp.core.model.CoverageMethod;
import nl.uva.yamp.core.model.TestCase;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.data.ExecutionDataStore;

import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class JacocoCoverageCollector implements CoverageCollector {

    private static final Pattern PATTERN = Pattern.compile("(.*)(\\[.*])");
    private static final String CONSTRUCTOR_NAME = "<init>";
    private final Path projectDirectory;
    private final JacocoCoverageConfiguration configuration;
    private final JacocoFileParser jacocoFileParser;
    private final TargetDirectoryLocator targetDirectoryLocator;
    private final ClassFileLoader classFileLoader;

    @Override
    @SneakyThrows
    public Set<Coverage> collect() {
        Set<TargetDirectory> targetDirectories = targetDirectoryLocator.findTargetDirectories(projectDirectory);
        return targetDirectories.stream()
            .map(this::readModule)
            .flatMap(Set::stream)
            .collect(Collectors.toSet());
    }

    private Set<Coverage> readModule(TargetDirectory targetDirectory) {
        log.info("Discovered module: {}", targetDirectory.getModuleName());

        Map<String, ExecutionDataStore> jacocoData = jacocoFileParser.readJacocoExec(targetDirectory.getPath());

        Set<Path> classFiles = classFileLoader.getClassFiles(targetDirectory.getPath().resolve("classes"));

        return (configuration.getParallel() ? jacocoData.entrySet().parallelStream() : jacocoData.entrySet().stream())
            .filter(pair -> isValidSessionId(pair.getKey()))
            .map(pair -> collectTestCaseData(pair.getKey(), pair.getValue(), classFiles))
            .collect(Collectors.toSet());
    }

    private boolean isValidSessionId(String sessionId) {
        return sessionId != null && sessionId.contains("#");
    }

    private Coverage collectTestCaseData(String sessionId, ExecutionDataStore executionDataStore, Set<Path> classFiles) {
        CoverageBuilder coverageBuilder = getCoverageBuilder(executionDataStore, classFiles);
        TestCase testCase = getTestCase(sessionId);
        Set<JacocoMethod> coveredMethods = getCoveredMethods(coverageBuilder);
        return Coverage.builder()
            .testCase(testCase)
            .constructors(filterConstructors(coveredMethods))
            .methods(filterMethods(coveredMethods))
            .build();
    }

    @SneakyThrows
    private CoverageBuilder getCoverageBuilder(ExecutionDataStore executionDataStore, Set<Path> classFiles) {
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);
        for (Path classFile : classFiles) {
            try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(classFile))) {
                analyzer.analyzeClass(inputStream, classFile.toFile().getPath());
            }
        }
        return coverageBuilder;
    }

    private TestCase getTestCase(String sessionId) {
        String fullyQualifiedClassName = sessionId.split("#")[0];
        String methodName = sessionId.split("#")[1];
        Matcher matcher = PATTERN.matcher(methodName);
        if (matcher.find()) {
            return TestCase.builder()
                .packageName(getPackageName(fullyQualifiedClassName))
                .className(getClassName(fullyQualifiedClassName))
                .methodName(matcher.group(1))
                .identifier(matcher.group(2))
                .build();
        } else {
            return TestCase.builder()
                .packageName(getPackageName(fullyQualifiedClassName))
                .className(getClassName(fullyQualifiedClassName))
                .methodName(methodName)
                .build();
        }
    }

    private String getPackageName(String fullyQualifiedClassName) {
        return fullyQualifiedClassName.substring(0, fullyQualifiedClassName.lastIndexOf('.'));
    }

    private String getClassName(String fullyQualifiedClassName) {
        return fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf('.') + 1);
    }

    private Set<JacocoMethod> getCoveredMethods(CoverageBuilder coverageBuilder) {
        return coverageBuilder.getClasses()
            .stream()
            .filter(classCoverage -> classCoverage.getClassCounter().getCoveredCount() == 1)
            .flatMap(this::getCoveredMethods)
            .collect(Collectors.toSet());
    }

    private Stream<JacocoMethod> getCoveredMethods(IClassCoverage classCoverage) {
        return classCoverage.getMethods()
            .stream()
            .filter(methodCoverage -> methodCoverage.getMethodCounter().getCoveredCount() == 1)
            .map(methodCoverage -> getCoveredMethod(classCoverage, methodCoverage));
    }

    private JacocoMethod getCoveredMethod(IClassCoverage classCoverage, IMethodCoverage methodCoverage) {
        String packageName = classCoverage.getPackageName().replace("/", ".");
        String className = classCoverage.getName().replace(classCoverage.getPackageName() + "/", "");
        String methodName = methodCoverage.getName();
        int loc = methodCoverage.getLineCounter().getTotalCount();
        return JacocoMethod.builder()
            .packageName(packageName)
            .className(className)
            .methodName(methodName)
            .descriptor(methodCoverage.getDesc())
            .loc(loc)
            .build();
    }

    private Set<CoverageConstructor> filterConstructors(Set<JacocoMethod> coveredMethods) {
        return coveredMethods.stream()
            .filter(method -> method.getMethodName().equals(CONSTRUCTOR_NAME))
            .map(method -> CoverageConstructor.builder()
                .packageName(method.getPackageName())
                .className(method.getClassName())
                .descriptor(method.getDescriptor())
                .loc(method.getLoc())
                .build())
            .collect(Collectors.toSet());
    }

    private Set<CoverageMethod> filterMethods(Set<JacocoMethod> coveredMethods) {
        return coveredMethods.stream()
            .filter(method -> !method.getMethodName().equals(CONSTRUCTOR_NAME))
            .map(method -> CoverageMethod.builder()
                .packageName(method.getPackageName())
                .className(method.getClassName())
                .methodName(method.getMethodName())
                .descriptor(method.getDescriptor())
                .loc(method.getLoc())
                .build())
            .collect(Collectors.toSet());
    }

    @Getter
    @Builder
    @EqualsAndHashCode
    private static class JacocoMethod {

        @NonNull
        private final String packageName;
        @NonNull
        private final String className;
        @NonNull
        private final String methodName;
        @NonNull
        private final String descriptor;
        @NonNull
        private final Integer loc;
    }
}
