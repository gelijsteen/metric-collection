package nl.uva.yamp.reader.jacoco;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ProjectResourceLoader {

    private final JacocoReaderConfiguration configuration;

    @SneakyThrows
    Set<File> getClassFiles() {
        Path classesDirectory = new File(configuration.getTargetDirectory() + "classes").toPath();
        try (Stream<Path> paths = Files.find(classesDirectory, Integer.MAX_VALUE, this::isClassFile)) {
            return paths.map(Path::toFile).collect(Collectors.toSet());
        }
    }

    private boolean isClassFile(Path path, BasicFileAttributes basicFileAttributes) {
        return basicFileAttributes.isRegularFile() && path.getFileName().toString().endsWith(".class");
    }
}
