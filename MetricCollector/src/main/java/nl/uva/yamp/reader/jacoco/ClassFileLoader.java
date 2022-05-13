package nl.uva.yamp.reader.jacoco;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassFileLoader {

    @SneakyThrows
    Set<Path> getClassFiles(Path targetDirectory) {
        Path classesDirectory = targetDirectory.resolve("classes");
        try (Stream<Path> paths = Files.find(classesDirectory, Integer.MAX_VALUE, this::isClassFile)) {
            return paths.collect(Collectors.toSet());
        }
    }

    private boolean isClassFile(Path path, BasicFileAttributes basicFileAttributes) {
        return basicFileAttributes.isRegularFile() && path.getFileName().toString().endsWith(".class");
    }
}
