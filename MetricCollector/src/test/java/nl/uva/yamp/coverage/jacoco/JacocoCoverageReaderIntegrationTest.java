package nl.uva.yamp.coverage.jacoco;

import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.coverage.CoverageTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JacocoCoverageReaderIntegrationTest {

    private final JacocoCoverageConfiguration configuration = new JacocoCoverageConfiguration();
    private final JacocoFileParser jacocoFileParser = new JacocoFileParser();
    private final TargetDirectoryLocator targetDirectoryLocator = new TargetDirectoryLocator();
    private final ClassFileLoader classFileLoader = new ClassFileLoader();
    private final JacocoCoverageReader sut = new JacocoCoverageReader(Paths.get("src/test/resources"), configuration, jacocoFileParser, targetDirectoryLocator, classFileLoader);

    @BeforeEach
    void setUp() {
        configuration.setParallel(false);
    }

    @Test
    void happyFlow() {
        Set<Coverage> result = sut.read();

        assertThat(result).containsExactlyInAnyOrder(
            CoverageTestData.coverageBuilder()
                .testMethod(CoverageTestData.methodBuilder()
                    .className("UnitTest")
                    .methodName("test1")
                    .build())
                .constructors(Set.of(
                    CoverageTestData.constructorBuilder()
                        .className("Direct")
                        .build(),
                    CoverageTestData.constructorBuilder()
                        .className("Indirect")
                        .build()
                ))
                .methods(Set.of(
                    CoverageTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    CoverageTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build()
                ))
                .build(),
            CoverageTestData.coverageBuilder()
                .testMethod(CoverageTestData.methodBuilder()
                    .className("UnitTest")
                    .methodName("test2")
                    .build())
                .constructors(Set.of(
                    CoverageTestData.constructorBuilder()
                        .className("Direct")
                        .build(),
                    CoverageTestData.constructorBuilder()
                        .className("Indirect")
                        .build()
                ))
                .methods(Set.of(
                    CoverageTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    CoverageTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build()
                ))
                .build(),
            CoverageTestData.coverageBuilder()
                .testMethod(CoverageTestData.methodBuilder()
                    .className("UnitTest")
                    .methodName("test3")
                    .build())
                .constructors(Set.of(
                    CoverageTestData.constructorBuilder()
                        .className("Direct")
                        .build(),
                    CoverageTestData.constructorBuilder()
                        .className("Indirect")
                        .build()
                ))
                .methods(Set.of(
                    CoverageTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    CoverageTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build()
                ))
                .build()
        );
    }
}