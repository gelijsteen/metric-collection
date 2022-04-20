package nl.uva.yamp;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessorTest {

    private static final String EXPECTED_PROFILES =
        "<profiles>\n" +
        "    <profile>\n" +
        "        <id>test-analysis</id>\n" +
        "        <build>\n" +
        "            <plugins>\n" +
        "                <plugin>\n" +
        "                    <groupId>org.jacoco</groupId>\n" +
        "                    <artifactId>jacoco-maven-plugin</artifactId>\n" +
        "                </plugin>\n" +
        "                <plugin>\n" +
        "                    <groupId>org.apache.maven.plugins</groupId>\n" +
        "                    <artifactId>maven-surefire-plugin</artifactId>\n" +
        "                </plugin>\n" +
        "            </plugins>\n" +
        "        </build>\n" +
        "        <dependencies>\n" +
        "            <dependency>\n" +
        "                <groupId>org.uva</groupId>\n" +
        "                <artifactId>JacocoListener</artifactId>\n" +
        "            </dependency>\n" +
        "        </dependencies>\n" +
        "    </profile>\n" +
        "</profiles>";

    private static final String EXPECTED_PROFILE =
        "<profile>\n" +
        "    <id>test-analysis</id>\n" +
        "    <build>\n" +
        "        <plugins>\n" +
        "            <plugin>\n" +
        "                <groupId>org.jacoco</groupId>\n" +
        "                <artifactId>jacoco-maven-plugin</artifactId>\n" +
        "            </plugin>\n" +
        "            <plugin>\n" +
        "                <groupId>org.apache.maven.plugins</groupId>\n" +
        "                <artifactId>maven-surefire-plugin</artifactId>\n" +
        "            </plugin>\n" +
        "        </plugins>\n" +
        "    </build>\n" +
        "    <dependencies>\n" +
        "        <dependency>\n" +
        "            <groupId>org.uva</groupId>\n" +
        "            <artifactId>JacocoListener</artifactId>\n" +
        "        </dependency>\n" +
        "    </dependencies>\n" +
        "</profile>";

    private final Processor sut = new Processor();

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"missing_profiles.xml", "empty_profiles.xml", "minimal_profile.xml", "unchanged_profile.xml"})
    void whenProfiles_expectValidProfilesElement(String fileName) {
        File profileFile = new File("src/test/resources/profile.xml");
        File pomFile = new File("src/test/resources/" + fileName);
        StringWriter stringWriter = new StringWriter();

        sut.process(profileFile, pomFile, stringWriter);

        assertThat(stringWriter.toString()).containsIgnoringWhitespaces(EXPECTED_PROFILES);
    }

    @Test
    @SneakyThrows
    void whenUnknownProfilePresent_expectValidProfileAdded() {
        File profileFile = new File("src/test/resources/profile.xml");
        File pomFile = new File("src/test/resources/unknown_profile.xml");
        StringWriter stringWriter = new StringWriter();

        sut.process(profileFile, pomFile, stringWriter);

        assertThat(stringWriter.toString()).containsIgnoringWhitespaces(EXPECTED_PROFILE);
    }

    @Test
    @SneakyThrows
    void whenMultipleProfilesPresent_expectValidProfileAdded() {
        File profileFile = new File("src/test/resources/profile.xml");
        File pomFile = new File("src/test/resources/pre_profile.xml");
        StringWriter stringWriter = new StringWriter();

        sut.process(profileFile, pomFile, stringWriter);

        assertThat(stringWriter.toString()).containsIgnoringWhitespaces(EXPECTED_PROFILE);
    }
}