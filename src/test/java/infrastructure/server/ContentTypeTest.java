package infrastructure.server;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ContentTypeTest {
    @Test
    public void should_return_content_type_with_the_same_description() {
        //Given
        String contentType = "application/json";

        //When
        ContentType result = ContentType.byValue(contentType);

        //Then
        assertThat(result).isEqualTo(ContentType.JSON);
    }

    @Test
    public void the_content_type_should_not_be_supported()  {
        //Given
        String contentType = "x";

        //When
        boolean supported = ContentType.isSupportedContentType(contentType);

        //Then
        assertThat(supported).isFalse();
    }

    @Test
    public void the_content_type_should_be_supported()  {
        //Given
        String contentType = "application/json";

        //When
        boolean supported = ContentType.isSupportedContentType(contentType);

        //Then
        assertThat(supported).isTrue();
    }

}