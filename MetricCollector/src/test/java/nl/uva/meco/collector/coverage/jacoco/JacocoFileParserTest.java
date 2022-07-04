package nl.uva.meco.collector.coverage.jacoco;

import org.jacoco.core.data.ExecutionDataStore;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JacocoFileParserTest {

    private final JacocoFileParser sut = new JacocoFileParser();

    @Test
    void happyFlow() {
        Map<String, ExecutionDataStore> result = sut.readJacocoExec(Paths.get("src/test/resources/reference/target"));

        assertThat(result).containsKey("test.pkg.UnitTest#test1");
    }
}