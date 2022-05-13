package nl.uva.yamp.reader.jacoco;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.ExecutionDataStore;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JacocoFileParser {

    private ExecutionDataStore executionDataStore;

    @SneakyThrows
    Map<String, ExecutionDataStore> readJacocoExec(Path targetDirectory) {
        Map<String, ExecutionDataStore> map = new HashMap<>();
        try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(targetDirectory.resolve("jacoco.exec")))) {
            ExecutionDataReader reader = new ExecutionDataReader(inputStream);
            reader.setSessionInfoVisitor(sessionInfo -> executionDataStore = map.computeIfAbsent(sessionInfo.getId(), key -> new ExecutionDataStore()));
            reader.setExecutionDataVisitor(executionData -> executionDataStore.put(executionData));
            reader.read();
        }
        return map;
    }
}
