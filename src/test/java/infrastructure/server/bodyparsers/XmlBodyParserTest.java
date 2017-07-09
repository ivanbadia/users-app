package infrastructure.server.bodyparsers;

import infrastructure.server.EntityForTesting;
import infrastructure.server.exceptions.BadRequestException;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class XmlBodyParserTest {

    private final XmlBodyParser xmlBodyParser = new XmlBodyParser();

    @Test
    public void should_parse_a_valid_xml(){
        //Given
        int id = 1;
        String message = "some message";
        String xml = "<entity><id>"+id+"</id><message>"+message+"</message></entity>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(xml.getBytes());


        //When
        EntityForTesting result = xmlBodyParser.parse(inputStream, EntityForTesting.class);

        //Then
        assertThat(result)
                .matches(entity -> entity.id==id)
                .matches(entity -> entity.message.equals(message));
    }


    @Test
    public void should_fail_if_the_xml_is_invalid(){
        //Given: An invalid xml
        String invalidXml = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidXml.getBytes());

        //Then
        assertThatThrownBy(() -> xmlBodyParser.parse(inputStream, EntityForTesting.class))
                .isInstanceOf(BadRequestException.class);

    }



}