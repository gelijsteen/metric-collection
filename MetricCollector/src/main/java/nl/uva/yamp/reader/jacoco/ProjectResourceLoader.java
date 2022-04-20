package nl.uva.yamp.reader.jacoco;

import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectResourceLoader {

    @SneakyThrows
    Set<Module> getTargetDirectories(String rootDirectory) {
        Path classesDirectory = new File(rootDirectory).toPath();
        try (Stream<Path> paths = Files.find(classesDirectory, Integer.MAX_VALUE, this::isJacocoTargetDirectory)) {
            return paths.map(this::createModule).collect(Collectors.toSet());
        }
    }

    private boolean isJacocoTargetDirectory(Path path, BasicFileAttributes basicFileAttributes) {
        return path.getFileName().toString().equals("target") &&
            path.resolve("jacoco.exec").toFile().exists() &&
            path.resolve("classes").toFile().exists();
    }

    private Module createModule(Path path) {
        return Module.builder()
            .targetDirectory(path.toAbsolutePath().toString())
            .name(path.getParent().getFileName().toString())
            .build();
    }

    @SneakyThrows
    Set<File> getClassFiles(String targetDirectory) {
        Path classesDirectory = new File(targetDirectory + "/classes").toPath();
        try (Stream<Path> paths = Files.find(classesDirectory, Integer.MAX_VALUE, this::isClassFile)) {
            return paths.map(Path::toFile).collect(Collectors.toSet());
        }
    }

    private boolean isClassFile(Path path, BasicFileAttributes basicFileAttributes) {
        return basicFileAttributes.isRegularFile() && path.getFileName().toString().endsWith(".class");
    }
}
