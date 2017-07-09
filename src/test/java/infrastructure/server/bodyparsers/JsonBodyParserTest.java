package infrastructure.server.bodyparsers;

import infrastructure.server.EntityForTesting;
import infrastructure.server.exceptions.BadRequestException;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JsonBodyParserTest {

    private JsonBodyParser jsonBodyParser = new JsonBodyParser();

    @Test
    public void should_parse_a_valid_json(){
        //Given
        int id = 1;
        String message = "some message";
        String json = "{\"id\":"+id+",\"message\":\""+message+"\"}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes());


        //When
        EntityForTesting result = jsonBodyParser.parse(inputStream, EntityForTesting.class);

        //Then
        assertThat(result)
                .matches(entity -> entity.id==id)
                .matches(entity -> entity.message.equals(message));
    }


    @Test
    public void should_fail_when_the_json_is_invalid(){
        //Given: An invalid JSON
        String invalidJson = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidJson.getBytes());

        //Then
        assertThatThrownBy(() -> jsonBodyParser.parse(inputStream, EntityForTesting.class))
            .isInstanceOf(BadRequestException.class);

    }


}