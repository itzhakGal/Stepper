package xmlReaderJavaFX.schema;


import xmlReaderJavaFX.schema.generated.STStepper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SchemaBasedJAXBMainJavaFX
{
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "xmlReaderJavaFX.schema.generated";

    public xmlReaderJavaFX.schema.generated.STStepper createStepperFromXMLFileForJavaFX(File file)  {
        xmlReaderJavaFX.schema.generated.STStepper stepper = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            stepper = deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return stepper;
    }

    public xmlReaderJavaFX.schema.generated.STStepper createStepperFromXMLFileForWeb(InputStream inputStreamFile)  {
        xmlReaderJavaFX.schema.generated.STStepper stepper = null;
        try {
            //InputStream inputStream = new FileInputStream(file);
            stepper = deserializeFrom(inputStreamFile);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return stepper;
    }
    private static xmlReaderJavaFX.schema.generated.STStepper deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (xmlReaderJavaFX.schema.generated.STStepper) u.unmarshal(in);
    }
}
