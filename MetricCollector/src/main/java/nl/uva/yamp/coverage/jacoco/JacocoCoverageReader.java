package nl.uva.yamp.coverage.jacoco;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.uva.yamp.core.coverage.CoverageReader;
import nl.uva.yamp.core.model.Constructor;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.data.ExecutionDataStore;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class JacocoCoverageReader implements CoverageReader {

    private static final String CONSTRUCTOR_NAME = "<init>";
    private final Path projectDirectory;
    private final JacocoCoverageConfiguration configuration;
    private final JacocoFileParser jacocoFileParser;
    private final TargetDirectoryLocator targetDirectoryLocator;
    private final ClassFileLoader classFileLoader;

    @Inject
    public JacocoCoverageReader(@Named("projectDirectory") Path projectDirectory,
                                JacocoCoverageConfiguration configuration,
                                JacocoFileParser jacocoFileParser,
                                TargetDirectoryLocator targetDirectoryLocator,
                                ClassFileLoader classFileLoader) {
        this.projectDirectory = projectDirectory;
        this.configuration = configuration;
        this.jacocoFileParser = jacocoFileParser;
        this.targetDirectoryLocator = targetDirectoryLocator;
        this.classFileLoader = classFileLoader;
    }

    @Override
    @SneakyThrows
    public Set<Coverage> read() {
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
            .map(pair -> collectTestMethodData(pair.getKey(), pair.getValue(), classFiles))
            .collect(Collectors.toSet());
    }

    private boolean isValidSessionId(String sessionId) {
        return sessionId != null && sessionId.contains("#");
    }

    private Coverage collectTestMethodData(String sessionId, ExecutionDataStore executionDataStore, Set<Path> classFiles) {
        CoverageBuilder coverageBuilder = getCoverageBuilder(executionDataStore, classFiles);
        Method testMethod = getTestMethod(sessionId);
        Set<JacocoMethod> coveredMethods = getCoveredMethods(coverageBuilder);
        return Coverage.builder()
            .testMethod(testMethod)
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

    private Method getTestMethod(String sessionId) {
        String fullyQualifiedClassName = sessionId.split("#")[0];
        return Method.builder()
            .packageName(getPackageName(fullyQualifiedClassName))
            .className(getClassName(fullyQualifiedClassName))
            .methodName(sessionId.split("#")[1])
            .build();
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
        return JacocoMethod.builder()
            .packageName(packageName)
            .className(className)
            .methodName(methodName)
            .build();
    }

    private Set<Constructor> filterConstructors(Set<JacocoMethod> coveredMethods) {
        return coveredMethods.stream()
            .filter(method -> method.getMethodName().equals(CONSTRUCTOR_NAME))
            .map(method -> Constructor.builder()
                .packageName(method.getPackageName())
                .className(method.getClassName())
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
    }
}
