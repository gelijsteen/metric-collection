package nl.uva.yamp.collector.module.maven;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import nl.uva.yamp.core.collector.TargetCollector;
import nl.uva.yamp.core.model.TargetDirectory;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
class MavenTargetCollector implements TargetCollector {

    private final Path projectDirectory;

    @Override
    @SneakyThrows
    public Set<TargetDirectory> collect() {
        try (Stream<Path> paths = Files.find(projectDirectory, Integer.MAX_VALUE, this::isJacocoTargetDirectory)) {
            return paths.map(this::mapPathToTargetDirectory).collect(Collectors.toSet());
        }
    }

    private boolean isJacocoTargetDirectory(Path path, BasicFileAttributes basicFileAttributes) {
        return basicFileAttributes.isDirectory() &&
            path.getFileName().toString().equals("target") &&
            path.resolve("classes").toFile().exists() &&
            path.resolve("test-classes").toFile().exists() &&
            path.resolve("jacoco.exec").toFile().exists();
    }

    private TargetDirectory mapPathToTargetDirectory(Path path) {
        return TargetDirectory.builder()
            .path(path)
            .moduleName(path.getParent().getFileName().toString())
            .build();
    }
}
