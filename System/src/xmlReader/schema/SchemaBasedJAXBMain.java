package xmlReader.schema;


import xmlReader.schema.generated.STStepper;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SchemaBasedJAXBMain
{
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "xmlReader.schema.generated";

    public STStepper createStepperFromXMLFile(File file)  {
        STStepper stepper = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            stepper = deserializeFrom(inputStream);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return stepper;
    }
    private static STStepper deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (STStepper) u.unmarshal(in);
    }
}
