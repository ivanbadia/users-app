package application.user.representation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.model.user.Role;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;


public class UserRepresentationSerializationTest {


    @Test
    public void should_serialize_a_user_as_json() throws IOException {
        //Given
        UserRepresentation user = userRepresentation();

        //When
        String json = serializeToJson(user);


        //Then
        assertThat(json)
                .isEqualTo("{\"id\":1,\"username\":\"username\",\"roles\":[\"ADMIN\"]}");

    }

    private String serializeToJson(UserRepresentation user) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(user);
    }

    private UserRepresentation userRepresentation() {
        return  new UserRepresentation(1, "username", Collections.singleton(Role.ADMIN));
    }



    @Test
    public void should_serialize_a_user_as_xml() throws Exception {

        //Given
        UserRepresentation user = userRepresentation();

        //When
        String xml = serializeToXml(user);

        //Then
        assertThat(xml)
                .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><user><id>1</id><username>username</username><roles><role>ADMIN</role></roles></user>");


    }

    private String serializeToXml(UserRepresentation user) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(UserRepresentation.class);
        Marshaller m = jc.createMarshaller();
        StringWriter sw = new StringWriter();
        m.marshal(user, sw);
        return sw.toString();
    }
}
