package nl.uva.yamp.coverage.jacoco;

import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TargetDirectoryLocator {

    @SneakyThrows
    Set<TargetDirectory> findTargetDirectories(String rootDirectory) {
        Path classesDirectory = new File(rootDirectory).toPath();
        try (Stream<Path> paths = Files.find(classesDirectory, Integer.MAX_VALUE, this::isJacocoTargetDirectory)) {
            return paths.map(this::mapPathToTargetDirectory).collect(Collectors.toSet());
        }
    }

    private boolean isJacocoTargetDirectory(Path path, BasicFileAttributes basicFileAttributes) {
        return basicFileAttributes.isDirectory() &&
            path.getFileName().toString().equals("target") &&
            path.resolve("jacoco.exec").toFile().exists() &&
            path.resolve("classes").toFile().exists();
    }

    private TargetDirectory mapPathToTargetDirectory(Path path) {
        return TargetDirectory.builder()
            .path(path)
            .moduleName(path.getParent().getFileName().toString())
            .build();
    }
}
