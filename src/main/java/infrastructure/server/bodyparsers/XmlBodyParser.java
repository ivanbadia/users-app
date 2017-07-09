package infrastructure.server.bodyparsers;


import infrastructure.server.exceptions.BadRequestException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class XmlBodyParser implements BodyParser{

    @Override
    public <T> T parse(InputStream inputStream, Class<T> classOfT) {
        try {
            JAXBContext context = JAXBContext.newInstance(classOfT);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(inputStream);
        } catch (Exception e) {
            throw new BadRequestException("The Xml is not valid", e);
        }



    }
}
