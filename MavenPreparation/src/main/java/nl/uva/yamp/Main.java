package nl.uva.yamp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;

@Slf4j
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        if (args.length != 2) {
            log.info("Usage: <profile.xml> <pom.xml>");
            System.exit(1);
        }

        File profileFile = new File(args[0]);
        if (!profileFile.exists()) {
            log.warn("File [{}] does not exist.", args[0]);
            System.exit(2);
        }

        File pomFile = new File(args[1]);
        if (!pomFile.exists()) {
            log.warn("File [{}] does not exist.", args[1]);
            System.exit(3);
        }

        try (FileWriter fileWriter = new FileWriter(pomFile)) {
            new Processor().process(profileFile, pomFile, fileWriter);
            fileWriter.flush();
        }

        System.exit(0);
    }
}
