package nl.uva.yamp.collector.coverage.jacoco;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(onConstructor = @__(@Inject))
public class ClassFileLoader {

    @SneakyThrows
    Set<Path> getClassFiles(Path directory) {
        try (Stream<Path> paths = Files.find(directory, Integer.MAX_VALUE, this::isClassFile)) {
            return paths.collect(Collectors.toSet());
        }
    }

    private boolean isClassFile(Path path, BasicFileAttributes basicFileAttributes) {
        return basicFileAttributes.isRegularFile() && path.getFileName().toString().endsWith(".class");
    }
}
