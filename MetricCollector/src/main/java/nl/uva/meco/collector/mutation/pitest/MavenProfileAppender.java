package nl.uva.meco.collector.mutation.pitest;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.inject.Inject;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;

@NoArgsConstructor(onConstructor = @__(@Inject))
class MavenProfileAppender {

    @SneakyThrows
    String addProfileToPomFile(String pom, String profile) {
        SAXReader saxReader = new SAXReader();

        Document pomDocument = saxReader.read(new StringReader(pom));
        Element pomRootElement = pomDocument.getRootElement();

        Document profileDocument = saxReader.read(new StringReader(profile));
        Element profileRootElement = profileDocument.getRootElement();

        Element profilesElement = getProfilesElement(pomRootElement);

        String profileId = getProfileId(profileRootElement);
        removeExistingProfileByProfileId(profilesElement, profileId);
        profilesElement.add(profileRootElement);

        StringWriter stringWriter = new StringWriter();
        pomDocument.write(stringWriter);
        return stringWriter.toString();
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
