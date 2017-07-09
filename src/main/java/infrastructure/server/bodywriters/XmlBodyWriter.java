package infrastructure.server.bodywriters;


import infrastructure.server.exceptions.InternalServerException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.OutputStream;

public class XmlBodyWriter implements BodyWriter{

    @Override
    public void write(Object object, OutputStream os) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(object, os);
        } catch (Exception e) {
            throw new InternalServerException("Error writing xml", e);
        }
    }
}
