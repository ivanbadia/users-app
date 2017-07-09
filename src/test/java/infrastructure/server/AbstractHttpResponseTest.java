package infrastructure.server;

import org.junit.Test;

import java.net.HttpCookie;
import java.util.Optional;

import static infrastructure.server.constants.Headers.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;


public class AbstractHttpResponseTest {

    @Test
    public void header_should_be_added(){
        //Given
        MockHttpResponse httpResponse =  new MockHttpResponse();
        String header = "header";
        String value = "value";

        //When
        httpResponse.addHeader(header, value);

        //Then
        assertThat(httpResponse.headers().get(header))
                .isNotNull()
                .isEqualTo(value);
    }


    @Test
    public void content_type_should_be_added(){
        //Given
        MockHttpResponse httpResponse =  new MockHttpResponse();

        //When
        httpResponse.setContentType(ContentType.HTML);

        //Then
        assertThat(httpResponse.headers().get(CONTENT_TYPE))
                .isNotNull()
                .contains(ContentType.HTML.value());
    }

    @Test
    public void cookie_should_be_added(){
        //Given
        MockHttpResponse httpResponse =  new MockHttpResponse();
        HttpCookie cookie = new HttpCookie("name", "value");

        //When
        httpResponse.addCookie(cookie);

        //Then
        assertThat(httpResponse.headers().get("Set-Cookie"))
                .isNotNull()
                //the cookie should be httponly
                .isEqualTo(cookie.toString()+"; HttpOnly");
    }


    @Test
    public void content_type_should_be_read(){
        //Given
        MockHttpResponse httpResponse =  new MockHttpResponse();
        httpResponse.setContentType(ContentType.HTML);

        //When
        Optional<String> contentType = httpResponse.contentType();

        //Then
        assertThat(contentType)
                .isPresent()
                .contains(ContentType.HTML.value());
    }

}