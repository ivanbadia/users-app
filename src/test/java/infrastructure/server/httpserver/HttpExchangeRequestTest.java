package infrastructure.server.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import infrastructure.server.ContentType;
import infrastructure.server.HttpMethod;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

import static infrastructure.server.constants.Headers.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class HttpExchangeRequestTest {


    @Test
    public void should_create_a_new_instance() throws URISyntaxException {
        //Given
        HttpExchange httpExchange = mock(HttpExchange.class);
        String path = "/path";
        when(httpExchange.getRequestURI())
                .thenReturn(uriWith(path));
        when(httpExchange.getRequestMethod()).thenReturn(HttpMethod.GET.name());

        //When
        HttpExchangeRequest httpExchangeRequest = HttpExchangeRequest.from(httpExchange);

        //Then
        assertThat(httpExchangeRequest)
                .matches(request -> request.httpMethod().equals(HttpMethod.GET))
                .matches(request -> request.path().equals(path));

    }

    @Test
    public void should_read_body()  {
        //Given
        HttpExchange httpExchange = mockHttpExchange();

        ByteArrayInputStream expectedInputStream = new ByteArrayInputStream(new byte[0]);
        when(httpExchange.getRequestBody()).thenReturn(expectedInputStream);

        HttpExchangeRequest request = HttpExchangeRequest.from(httpExchange);

        //When
        InputStream inputStream = request.body();

        //Then
        assertThat(inputStream)
                .isEqualTo(expectedInputStream);

    }

    private HttpExchange mockHttpExchange()  {
        HttpExchange httpExchange = mock(HttpExchange.class);

        when(httpExchange.getRequestMethod()).thenReturn(HttpMethod.GET.name());
        when(httpExchange.getRequestURI())
                .thenReturn(uriWith("/path"));
        return httpExchange;
    }

    @Test
    public void should_read_content_type() {
        //Given
        HttpExchange httpExchange = mockHttpExchange();
        Headers headers = new Headers();
        headers.put(CONTENT_TYPE, Collections.singletonList("application/json; charset=utf-8"));
        when(httpExchange.getRequestHeaders()).thenReturn(headers);
        HttpExchangeRequest request = HttpExchangeRequest.from(httpExchange);


        //When
        Optional<ContentType> contentType = request.contentType();

        //Then
        assertThat(contentType)
                .isPresent()
                .contains(ContentType.JSON);

    }


    @Test
    public void should_read_header() {
        //Given
        HttpExchange httpExchange = mockHttpExchange();
        Headers headers = new Headers();
        String header = "Authorization";
        String headerValue = "Basic x";
        headers.put(header, Collections.singletonList(headerValue));
        when(httpExchange.getRequestHeaders()).thenReturn(headers);
        HttpExchangeRequest request = HttpExchangeRequest.from(httpExchange);


        //When
        Optional<String> value = request.header(header);

        //Then
        assertThat(value)
                .isPresent()
                .contains(headerValue);

    }


    private URI uriWith(String path) {
        try {
            return new URI("scheme", "authority", path,null,null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void should_return_post_parameter() throws UnsupportedEncodingException {

        //Given
        HttpExchange httpExchange = mockHttpExchange();
        when(httpExchange.getRequestMethod()).thenReturn(HttpMethod.POST.name());
        Headers headers = new Headers();
        headers.put(CONTENT_TYPE, Collections.singletonList("application/x-www-form-urlencoded"));
        when(httpExchange.getRequestHeaders()).thenReturn(headers);
        when(httpExchange.getRequestBody()).thenReturn(new ByteArrayInputStream("ParamOne=ValueOne&ParamTwo=ValueTwo".getBytes(StandardCharsets.UTF_8.name())));
        HttpExchangeRequest request = HttpExchangeRequest.from(httpExchange);

        //When
        Optional<String> value = request.parameter("ParamTwo");

        //Then
        assertThat(value)
                .isPresent()
                .contains("ValueTwo");

    }
}