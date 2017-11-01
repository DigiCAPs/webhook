package com.digicaps.openholo.jninka;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class JninkaWrapper {

    private final String RESULT_FILE_NAME = "result.xml";
    
    public String findLicense(String resultPath, String jninkaLibraryPath) throws IOException, InterruptedException {
        String resultFile = resultPath + "/" + RESULT_FILE_NAME;

        String[] command = {
                "java",
                "-jar",
                jninkaLibraryPath,
                resultPath,
                resultFile
        };
        
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.start().waitFor();

        return resultFile;
    }
    
    public JninkaResult toParser(String filePath) throws JDOMException, IOException {
        JninkaResult result = new JninkaResult();

        File inputFile = new File(filePath); 
        SAXBuilder saxBuilder = new SAXBuilder(); 
        Document document = saxBuilder.build(inputFile);
        
        Element root = document.getRootElement();
        result.scanTime = root.getAttribute("scanTime").getValue();
        
        List<Element> files = root.getChildren("File"); 
        for (Element element : files) {

            JninkaFiles jninkaFile = new JninkaFiles();
            jninkaFile.fileName = element.getChildText("fileName");

            List<Element> attributes = element.getChildren("attribution"); 

            for (Element elem : attributes) {
                JninkaAttribute attr = new JninkaAttribute(elem.getChildText("matchname"),
                                                           elem.getChildText("originalLine"));
                jninkaFile.attributes.add(attr);
            }
            
            result.files.add(jninkaFile);
        }

        return result; 
    }
}
