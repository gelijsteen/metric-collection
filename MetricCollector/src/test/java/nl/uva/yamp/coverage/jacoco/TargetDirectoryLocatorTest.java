package nl.uva.yamp.coverage.jacoco;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TargetDirectoryLocatorTest {

    private final TargetDirectoryLocator sut = new TargetDirectoryLocator();

    @Test
    void happyFlow() {
        Set<TargetDirectory> result = sut.findTargetDirectories(Paths.get("src/test/resources"));

        assertThat(result).containsExactly(TargetDirectory.builder()
            .path(Paths.get("src/test/resources/jacoco/target"))
            .moduleName("jacoco")
            .build());
    }
}