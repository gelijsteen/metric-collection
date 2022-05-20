package nl.uva.yamp.collector.coverage.jacoco;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ClassFileLoaderTest {

    private final ClassFileLoader sut = new ClassFileLoader();

    @Test
    void happyFlow() {
        Set<Path> result = sut.getClassFiles(Paths.get("src/test/resources/reference/target/classes"));

        assertThat(result).containsExactlyInAnyOrder(
            Paths.get("src/test/resources/reference/target/classes/test/pkg/Direct.class"),
            Paths.get("src/test/resources/reference/target/classes/test/pkg/Indirect.class"),
            Paths.get("src/test/resources/reference/target/classes/test/pkg/NonCovered.class")
        );
    }
}