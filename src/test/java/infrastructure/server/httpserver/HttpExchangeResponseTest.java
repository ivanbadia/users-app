package infrastructure.server.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import infrastructure.server.ContentType;
import infrastructure.server.EntityForTesting;
import infrastructure.server.HttpResponse;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;

import static infrastructure.server.constants.Headers.ACCEPT;
import static infrastructure.server.constants.Headers.CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HttpExchangeResponseTest {


    @Test
    public void should_create_a_new_instance() {
        //Given
        HttpExchange httpExchange = mock(HttpExchange.class);


        //When
        HttpResponse httpResponse = HttpExchangeResponse.from(httpExchange);

        //Then
        assertThat(httpResponse)
                .isNotNull();

    }


    @Test
    public void the_initial_content_type_should_be_read_from_Accept_Header() {
        //Given
        HttpExchange httpExchange = mock(HttpExchange.class);
        Headers headers = new Headers();
        headers.put(ACCEPT, Collections.singletonList("application/json, application/javascript, text/javascript"));
        when(httpExchange.getRequestHeaders()).thenReturn(headers);

        //When
        HttpResponse httpResponse = HttpExchangeResponse.from(httpExchange);

        //Then
        assertThat(httpResponse.contentType())
                .isPresent()
                .contains(ContentType.JSON.value());

    }



    @Test
    public void headers_should_be_sent_when_the_response_is_ended() throws IOException {
        //SetUp
        HttpExchange httpExchange = mockHttpExchange();

        //Given
        int statusCode = 404;
        HttpResponse httpResponse = HttpExchangeResponse.from(httpExchange)
                .setContentType(ContentType.HTML)
                .setStatusCode(statusCode);

        //When
        httpResponse.end();


        //Then
        verify(httpExchange).sendResponseHeaders(statusCode, 0);
        assertThat(httpExchange.getResponseHeaders())
                .containsKey(CONTENT_TYPE)
                .containsValue(Collections.singletonList(ContentType.HTML.value()));

    }

    private HttpExchange mockHttpExchange() {
        HttpExchange httpExchange = Mockito.mock(HttpExchange.class);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Mockito.when(httpExchange.getResponseBody()).thenReturn(output);
        Headers headers = new Headers();
        when(httpExchange.getResponseHeaders()).thenReturn(headers);
        return httpExchange;
    }


    @Test
    public void body_should_be_written_in_the_expected_format_when_response_is_ended(){
        //SetUp
        HttpExchange httpExchange = mockHttpExchange();

        //Given
        EntityForTesting entityForTesting = new EntityForTesting(1, "message");
        HttpResponse httpResponse = HttpExchangeResponse.from(httpExchange)
                .setContentType(ContentType.JSON)
                .setStatusCode(200)
                .setBody(entityForTesting);


        //When
        httpResponse.end();


        //Then
        assertThat(httpExchange.getResponseBody().toString())
                .isEqualTo("{\"id\":1,\"message\":\"message\"}");
    }


    @Test
    public void request_should_be_redirected() {
        //SetUp
        HttpExchange httpExchange = mockHttpExchange();

        //Given
        HttpResponse httpResponse = HttpExchangeResponse.from(httpExchange);

        //When
        httpResponse.redirect("/path");

        //Then
        assertThat(httpResponse)
                .matches(response -> response.statusCode()== HttpURLConnection.HTTP_MOVED_TEMP);
        assertThat(httpExchange.getResponseHeaders())
                .containsEntry("Location", Collections.singletonList("/path"));

    }
}