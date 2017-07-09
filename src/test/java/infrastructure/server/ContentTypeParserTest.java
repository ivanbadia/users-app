package infrastructure.server;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class ContentTypeParserTest {
    @Test
    public void supported_content_type_should_be_extracted_from_content_type_header(){
        //Given
        String contentType = "x; application/json; charset=UTF-8";

        //When
        Optional<ContentType> result = ContentTypeParser.extractContentTypeFrom(contentType);

        //Then
        assertThat(result).isPresent().contains(ContentType.JSON);

    }


    @Test
    public void should_not_extract_content_type_from_content_type_header_if_no_content_type_valid(){
        //Given
        String contentType = "x; y; charset=UTF-8";

        //When
        Optional<ContentType> result = ContentTypeParser.extractContentTypeFrom(contentType);

        //Then
        assertThat(result).isEmpty();

    }

    @Test
    public void supported_content_type_should_be_extracted_from_Accept_header(){
        //Given
        String accept = "application/javascript, text/javascript, application/json";

        //When
        Optional<ContentType> result = ContentTypeParser.extractAcceptedContentTypeFrom(accept);

        //Then
        assertThat(result).isPresent().contains(ContentType.JSON);

    }

    @Test
    public void should_not_extract_content_type_from_Accept_header_if_no_content_type_valid(){
        //Given
        String contentType = "application/javascript, text/javascript";

        //When
        Optional<ContentType> result = ContentTypeParser.extractContentTypeFrom(contentType);

        //Then
        assertThat(result).isEmpty();

    }



}