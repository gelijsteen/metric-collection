package nl.uva.yamp;

import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.Writer;
import java.util.Optional;

public class Processor {

    @SneakyThrows
    public void process(File profileFile, File pomFile, Writer writer) {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(pomFile);
        Element rootElement = document.getRootElement();
        Element profilesElement = getProfilesElement(rootElement);

        removeTestAnalysisProfile(profilesElement);
        addTestAnalysisProfile(saxReader, profileFile, profilesElement);

        document.write(writer);
    }

    private Element getProfilesElement(Element rootElement) {
        return Optional.ofNullable(rootElement.element("profiles"))
            .orElseGet(() -> rootElement.addElement("profiles"));
    }

    private void removeTestAnalysisProfile(Element profilesElement) {
        profilesElement.elements().stream()
            .filter(this::isTestAnalysisProfile)
            .findFirst()
            .ifPresent(profilesElement::remove);
    }

    private boolean isTestAnalysisProfile(Element element) {
        Element idElement = element.element("id");
        return idElement != null && idElement.getText().equals("test-analysis");
    }

    @SneakyThrows
    private void addTestAnalysisProfile(SAXReader saxReader, File profileFile, Element profilesElement) {
        Document document = saxReader.read(profileFile);
        Element rootElement = document.getRootElement();
        profilesElement.add(rootElement);
    }
}
