package infrastructure.server.bodywriters;

import infrastructure.server.EntityForTesting;
import infrastructure.server.exceptions.InternalServerException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class XmlBodyWriterTest {

    private XmlBodyWriter xmlBodyWriter = new XmlBodyWriter();

    @Test
    public void should_write_xml_content(){
        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        EntityForTesting entityForTesting = new EntityForTesting(1, "something");

        //When
        xmlBodyWriter.write(entityForTesting, outputStream);

        //Then
        assertThat(new String(outputStream.toByteArray()))
                .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><entity><id>1</id><message>something</message></entity>");

    }


    @Test
    public void should_fail_if_an_error_arise_serializing_to_xml(){
        //Given an object that throws an error getting one value
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Object entityForTesting = new Object(){
            public String getX(){
                throw new IllegalArgumentException("x");
            }
        };

        //Then an internal server exception is thrown
        assertThatThrownBy(() -> xmlBodyWriter.write(entityForTesting, outputStream))
            .isInstanceOf(InternalServerException.class);

    }

}