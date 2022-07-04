package nl.uva.meco;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        if (args.length != 2) {
            log.info("Usage: <profile.xml> <pom.xml>");
            System.exit(1);
        }

        Path profileFile = Paths.get(args[0]);
        if (!profileFile.toFile().exists()) {
            log.warn("Profile file [{}] does not exist.", args[0]);
            System.exit(2);
        }

        Path pomFile = Paths.get(args[1]);
        if (!pomFile.toFile().exists()) {
            log.warn("POM file [{}] does not exist.", args[1]);
            System.exit(3);
        }

        String pomContent = Files.readString(pomFile);
        String profileContent = Files.readString(profileFile);

        MavenProfileAppender mavenProfileAppender = new MavenProfileAppender();
        Files.writeString(pomFile, mavenProfileAppender.addProfileToPomFile(pomContent, profileContent));

        System.exit(0);
    }
}
