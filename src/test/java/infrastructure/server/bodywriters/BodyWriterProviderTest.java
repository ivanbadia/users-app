package infrastructure.server.bodywriters;

import infrastructure.server.ContentType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class BodyWriterProviderTest {
    private BodyWriterProvider bodyWriterProvider = BodyWriterProvider.instance();

    @Test
    public void should_return_the_text_writer_for_invalid_content_types(){

        //Given: An invalid content type
        String invalidContentType = "x";

        //When
        BodyWriter bodyWriter = bodyWriterProvider.bodyWriterFor(invalidContentType);

        //Then: The Json body writer is the default body writer
        assertThat(bodyWriter)
                .isNotNull()
                .isInstanceOf(TextBodyWriter.class);
    }

    @Test
    public void should_return_the_json_body_writer_for_json(){

        //Given
        String contentType = ContentType.JSON.value();

        //When
        BodyWriter bodyWriter = bodyWriterProvider.bodyWriterFor(contentType);

        //Then
        assertThat(bodyWriter)
                .isNotNull()
                .isInstanceOf(JsonBodyWriter.class);
    }


    @Test
    public void should_return_the_xml_body_writer_for_xml(){

        //Given
        String contentType = ContentType.XML.value();

        //When
        BodyWriter bodyWriter = bodyWriterProvider.bodyWriterFor(contentType);

        //Then
        assertThat(bodyWriter)
                .isNotNull()
                .isInstanceOf(XmlBodyWriter.class);
    }


    @Test
    public void should_return_the_html_body_writer_for_html(){

        //Given
        String contentType = ContentType.HTML.value();

        //When
        BodyWriter bodyWriter = bodyWriterProvider.bodyWriterFor(contentType);

        //Then
        assertThat(bodyWriter)
                .isNotNull()
                .isInstanceOf(TextBodyWriter.class);
    }


}