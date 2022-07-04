package nl.uva.meco.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationLoader {

    @SneakyThrows
    public static <T> T loadConfiguration(Class<T> clazz) {
        Path path = PathResolver.getPath("application.yml");
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            return new Yaml(representer).loadAs(bufferedReader, clazz);
        }
    }
}
