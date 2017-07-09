package infrastructure.server.bodywriters;

import infrastructure.server.EntityForTesting;
import infrastructure.server.exceptions.InternalServerException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class JsonBodyWriterTest {

    private JsonBodyWriter jsonBodyWriter = new JsonBodyWriter();

    @Test
    public void should_write_json_content(){
        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        EntityForTesting entityForTesting = new EntityForTesting(1, "something");

        //When
        jsonBodyWriter.write(entityForTesting, outputStream);

        //Then
        assertThat(new String(outputStream.toByteArray()))
                .isEqualTo("{\"id\":1,\"message\":\"something\"}");

    }


    @Test
    public void should_fail_if_an_error_arise_serializing_to_json(){
        //Given an object that throws an error getting one value
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Object entityForTesting = new Object(){
            public String getX(){
                throw new IllegalArgumentException("x");
            }
        };

        //Then an internal server exception is thrown
        assertThatThrownBy(() -> jsonBodyWriter.write(entityForTesting, outputStream))
            .isInstanceOf(InternalServerException.class);

    }

}