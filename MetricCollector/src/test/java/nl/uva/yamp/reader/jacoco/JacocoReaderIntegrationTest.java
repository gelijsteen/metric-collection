package nl.uva.yamp.reader.jacoco;

import nl.uva.yamp.core.model.Coverage;
import nl.uva.yamp.reader.ReaderTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JacocoReaderIntegrationTest {

    private final JacocoReaderConfiguration configuration = new JacocoReaderConfiguration();
    private final JacocoFileParser jacocoFileParser = new JacocoFileParser();
    private final TargetDirectoryLocator targetDirectoryLocator = new TargetDirectoryLocator();
    private final ClassFileLoader classFileLoader = new ClassFileLoader();
    private final JacocoReader sut = new JacocoReader(configuration, jacocoFileParser, targetDirectoryLocator, classFileLoader);

    @BeforeEach
    void setUp() {
        configuration.setProjectDirectory("src/test/resources");
        configuration.setParallel(false);
    }

    @Test
    void happyFlow() {
        Set<Coverage> result = sut.read();

        assertThat(result).containsExactlyInAnyOrder(
            ReaderTestData.coverageBuilder()
                .testMethod(ReaderTestData.methodBuilder()
                    .className("UnitTest")
                    .methodName("test1")
                    .build())
                .coveredMethods(Set.of(
                    ReaderTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    ReaderTestData.methodBuilder()
                        .className("Direct")
                        .methodName("<init>")
                        .build(),
                    ReaderTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build(),
                    ReaderTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("<init>")
                        .build()
                ))
                .build(),
            ReaderTestData.coverageBuilder()
                .testMethod(ReaderTestData.methodBuilder()
                    .className("UnitTest")
                    .methodName("test2")
                    .build())
                .coveredMethods(Set.of(
                    ReaderTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    ReaderTestData.methodBuilder()
                        .className("Direct")
                        .methodName("<init>")
                        .build(),
                    ReaderTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build(),
                    ReaderTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("<init>")
                        .build()
                ))
                .build(),
            ReaderTestData.coverageBuilder()
                .testMethod(ReaderTestData.methodBuilder()
                    .className("UnitTest")
                    .methodName("test3")
                    .build())
                .coveredMethods(Set.of(
                    ReaderTestData.methodBuilder()
                        .className("Direct")
                        .methodName("call")
                        .build(),
                    ReaderTestData.methodBuilder()
                        .className("Direct")
                        .methodName("<init>")
                        .build(),
                    ReaderTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("call")
                        .build(),
                    ReaderTestData.methodBuilder()
                        .className("Indirect")
                        .methodName("<init>")
                        .build()
                ))
                .build()
        );
    }
}