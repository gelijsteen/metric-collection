package nl.uva.yamp.mutation.pitest;

import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class MavenProfileAppender {

    @SneakyThrows
    public void append(Path pomFile, Path profileFile) {
        SAXReader saxReader = new SAXReader();

        Document pomDocument = saxReader.read(pomFile.toFile());
        Element pomRootElement = pomDocument.getRootElement();

        Document profileDocument = saxReader.read(profileFile.toFile());
        Element profileRootElement = profileDocument.getRootElement();

        Element profilesElement = getProfilesElement(pomRootElement);

        String profileId = getProfileId(profileRootElement);
        removeExistingProfileByProfileId(profilesElement, profileId);
        profilesElement.add(profileRootElement);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(pomFile)) {
            pomDocument.write(bufferedWriter);
        }
    }

    private Element getProfilesElement(Element rootElement) {
        return Optional.ofNullable(rootElement.element("profiles"))
            .orElseGet(() -> rootElement.addElement("profiles"));
    }

    private String getProfileId(Element element) {
        Element idElement = element.element("id");
        return idElement != null ? idElement.getStringValue() : null;
    }

    private void removeExistingProfileByProfileId(Element profilesElement, String id) {
        profilesElement.elements().stream()
            .filter(element -> isMatchByProfileId(element, id))
            .findFirst()
            .ifPresent(profilesElement::remove);
    }

    private boolean isMatchByProfileId(Element element, String id) {
        Element idElement = element.element("id");
        return idElement != null && idElement.getText().equals(id);
    }
}
