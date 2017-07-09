package infrastructure.server.bodywriters;

import infrastructure.server.EntityForTesting;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class TextBodyWriterTest {
    private TextBodyWriter textBodyWriter = new TextBodyWriter();
    @Test
    public void should_write_text_content(){
        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        EntityForTesting entityForTesting = new EntityForTesting(1, "something");

        //When
        textBodyWriter.write(entityForTesting, outputStream);

        //Then
        assertThat(new String(outputStream.toByteArray()))
                .isEqualTo(entityForTesting.toString());
    }

}