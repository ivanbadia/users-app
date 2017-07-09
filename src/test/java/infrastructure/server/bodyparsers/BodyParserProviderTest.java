package infrastructure.server.bodyparsers;

import infrastructure.server.ContentType;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class BodyParserProviderTest {
    private BodyParserProvider bodyParserProvider = BodyParserProvider.instance();


    @Test
    public void should_return_a_body_parser_for_json(){

        //Given
        ContentType contentType = ContentType.JSON;

        //When
        Optional<BodyParser> bodyParser = bodyParserProvider.bodyParserFor(contentType);

        //Then
        assertThat(bodyParser)
                .isPresent()
                .containsInstanceOf(JsonBodyParser.class);
    }


    @Test
    public void should_return_a_body_parser_for_xml(){

        //Given
        ContentType contentType = ContentType.XML;

        //When
        Optional<BodyParser> bodyParser = bodyParserProvider.bodyParserFor(contentType);

        //Then
        assertThat(bodyParser)
                .isPresent()
                .containsInstanceOf(XmlBodyParser.class);
    }


}