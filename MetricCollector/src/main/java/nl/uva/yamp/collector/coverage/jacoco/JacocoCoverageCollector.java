package nl.uva.yamp.collector.coverage.jacoco;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.collector.CoverageCollector;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.DataSet;
import nl.uva.yamp.core.model.Method;
import nl.uva.yamp.core.model.TargetDirectory;
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
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class JacocoCoverageCollector implements CoverageCollector {

    private static final Pattern PATTERN = Pattern.compile("(.*)(\\[.*])");
    private static final String CONSTRUCTOR_NAME = "<init>";
    private final ForkJoinPool forkJoinPool;
    private final JacocoFileParser jacocoFileParser;
    private final ClassFileLoader classFileLoader;

    @Override
    @SneakyThrows
    public Set<DataSet> collect(TargetDirectory targetDirectory) {
        log.info("Calculating coverage for module: {}", targetDirectory.getModuleName());

        Map<String, ExecutionDataStore> jacocoData = jacocoFileParser.readJacocoExec(targetDirectory.getPath());

        Set<Path> classes = classFileLoader.getClassFiles(targetDirectory.getPath().resolve("classes"));
        Set<Path> testClasses = classFileLoader.getClassFiles(targetDirectory.getPath().resolve("test-classes"));

        return forkJoinPool.submit(() -> jacocoData.entrySet().parallelStream()
                .filter(pair -> isValidSessionId(pair.getKey()))
                .map(pair -> collectTestCaseData(pair.getKey(), pair.getValue(), classes, testClasses))
                .collect(Collectors.toSet()))
            .get();
    }

    private boolean isValidSessionId(String sessionId) {
        return sessionId != null && sessionId.contains("#");
    }

    private DataSet collectTestCaseData(String sessionId,
                                        ExecutionDataStore executionDataStore,
                                        Set<Path> classes,
                                        Set<Path> testClasses) {
        CoverageBuilder coverageBuilder = getCoverageBuilder(executionDataStore, classes);
        CoverageBuilder testCoverageBuilder = getCoverageBuilder(executionDataStore, testClasses);
        TestCase testCase = getTestCase(sessionId);
        Set<JacocoMethod> coveredMethods = getCoveredMethods(coverageBuilder);
        Set<JacocoMethod> testCoverageMethods = getCoveredMethods(testCoverageBuilder);
        return DataSet.builder()
            .testCase(testCase)
            .constructors(filterConstructors(coveredMethods))
            .methods(filterMethods(coveredMethods))
            .testConstructors(filterConstructors(testCoverageMethods))
            .testMethods(filterMethods(testCoverageMethods))
            .mutations(Set.of())
            .build();
    }

    @SneakyThrows
    private CoverageBuilder getCoverageBuilder(ExecutionDataStore executionDataStore, Set<Path> classes) {
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);
        for (Path classFile : classes) {
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

    private Set<Constructor> filterConstructors(Set<JacocoMethod> coveredMethods) {
        return coveredMethods.stream()
            .filter(method -> method.getMethodName().equals(CONSTRUCTOR_NAME))
            .map(method -> Constructor.builder()
                .packageName(method.getPackageName())
                .className(method.getClassName())
                .descriptor(method.getDescriptor())
                .loc(method.getLoc())
                .direct(false)
                .build())
            .collect(Collectors.toSet());
    }

    private Set<Method> filterMethods(Set<JacocoMethod> coveredMethods) {
        return coveredMethods.stream()
            .filter(method -> !method.getMethodName().equals(CONSTRUCTOR_NAME))
            .map(method -> Method.builder()
                .packageName(method.getPackageName())
                .className(method.getClassName())
                .methodName(method.getMethodName())
                .descriptor(method.getDescriptor())
                .loc(method.getLoc())
                .direct(false)
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
