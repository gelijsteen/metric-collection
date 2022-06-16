package nl.uva.yamp;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class MavenProfileAppenderTest {

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

    private final MavenProfileAppender sut = new MavenProfileAppender();

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"missing_profiles.xml", "empty_profiles.xml", "minimal_profile.xml", "unchanged_profile.xml"})
    void whenProfiles_expectValidProfilesElement(String fileName) {
        Path pomFile = Paths.get("src/test/resources/" + fileName);
        Path profileFile = Paths.get("src/test/resources/profile.xml");

        String result = sut.addProfileToPomFile(Files.readString(pomFile), Files.readString(profileFile));

        assertThat(result).containsIgnoringWhitespaces(EXPECTED_PROFILE);
    }

    @Test
    @SneakyThrows
    void whenUnknownProfilePresent_expectValidProfileAdded() {
        Path pomFile = Paths.get("src/test/resources/unknown_profile.xml");
        Path profileFile = Paths.get("src/test/resources/profile.xml");

        String result = sut.addProfileToPomFile(Files.readString(pomFile), Files.readString(profileFile));

        assertThat(result).containsIgnoringWhitespaces(EXPECTED_PROFILE);
    }

    @Test
    @SneakyThrows
    void whenMultipleProfilesPresent_expectValidProfileAdded() {
        Path pomFile = Paths.get("src/test/resources/pre_profile.xml");
        Path profileFile = Paths.get("src/test/resources/profile.xml");

        String result = sut.addProfileToPomFile(Files.readString(pomFile), Files.readString(profileFile));

        assertThat(result).containsIgnoringWhitespaces(EXPECTED_PROFILE);
    }
}