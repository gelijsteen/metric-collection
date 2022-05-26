package nl.uva.yamp.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathResolver {

    public static Path getPath(String fileName) {
        Path configInWorkingDirectory = Paths.get(fileName);
        Path configInJar = getPathFromJar(fileName);
        return configInWorkingDirectory.toFile().exists() ? configInWorkingDirectory : configInJar;
    }

    private static Path getPathFromJar(String fileName) {
        return Optional.ofNullable(PathResolver.class.getClassLoader().getResource(fileName))
            .map(URL::getPath)
            .map(Paths::get)
            .orElseThrow();
    }
}
