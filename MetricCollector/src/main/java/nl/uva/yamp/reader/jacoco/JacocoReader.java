package nl.uva.yamp.reader.jacoco;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.uva.yamp.core.Reader;
import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.core.model.Method;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;
import org.jacoco.core.data.ExecutionDataStore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class JacocoReader implements Reader {

    private final JacocoReaderConfiguration configuration;
    private final JacocoFileParser jacocoFileParser;
    private final ProjectResourceLoader projectResourceLoader;

    @Override
    @SneakyThrows
    public Set<Coverage> read() {
        Map<String, ExecutionDataStore> jacocoData = jacocoFileParser.readJacocoExec();

        Set<File> files = projectResourceLoader.getClassFiles();

        return (configuration.getParallel() ? jacocoData.entrySet().parallelStream() : jacocoData.entrySet().stream())
            .filter(pair -> isValidSessionId(pair.getKey()))
            .map(pair -> collectTestMethodData(pair.getKey(), pair.getValue(), files))
            .collect(Collectors.toSet());
    }

    private boolean isValidSessionId(String sessionId) {
        return sessionId != null && sessionId.contains("#");
    }

    private Coverage collectTestMethodData(String sessionId, ExecutionDataStore executionDataStore, Set<File> files) {
        CoverageBuilder coverageBuilder = getCoverageBuilder(executionDataStore, files);
        Method testMethod = getTestMethod(sessionId);
        Set<Method> coveredMethods = getCoveredMethods(coverageBuilder);
        return Coverage.builder()
            .testMethod(testMethod)
            .coveredMethods(coveredMethods)
            .build();
    }

    @SneakyThrows
    private CoverageBuilder getCoverageBuilder(ExecutionDataStore executionDataStore, Set<File> files) {
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);
        for (File file : files) {
            try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
                analyzer.analyzeClass(inputStream, file.getPath());
            }
        }
        return coverageBuilder;
    }

    private Method getTestMethod(String sessionId) {
        String fullyQualifiedName = sessionId.split("#")[0];
        return Method.builder()
            .packageName(getPackageName(fullyQualifiedName))
            .className(getClassName(fullyQualifiedName))
            .methodName(sessionId.split("#")[1])
            .build();
    }

    private String getPackageName(String fullyQualifiedName) {
        return fullyQualifiedName.substring(0, fullyQualifiedName.lastIndexOf('.'));
    }

    private String getClassName(String fullyQualifiedName) {
        return fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf('.') + 1);
    }

    private Set<Method> getCoveredMethods(CoverageBuilder coverageBuilder) {
        return coverageBuilder.getClasses()
            .stream()
            .filter(classCoverage -> classCoverage.getClassCounter().getCoveredCount() == 1)
            .flatMap(this::getCoveredMethods)
            .collect(Collectors.toSet());
    }

    private Stream<Method> getCoveredMethods(IClassCoverage classCoverage) {
        return classCoverage.getMethods()
            .stream()
            .filter(methodCoverage -> methodCoverage.getMethodCounter().getCoveredCount() == 1)
            .map(methodCoverage -> getCoveredMethod(classCoverage, methodCoverage));
    }

    private Method getCoveredMethod(IClassCoverage classCoverage, IMethodCoverage methodCoverage) {
        String packageName = classCoverage.getPackageName().replace("/", ".");
        String className = classCoverage.getName().replace(classCoverage.getPackageName() + "/", "");
        String methodName = methodCoverage.getName();
        return Method.builder()
            .packageName(packageName)
            .className(className)
            .methodName(methodName)
            .build();
    }
}
