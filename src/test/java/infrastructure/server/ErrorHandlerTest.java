package infrastructure.server;

import infrastructure.server.exceptions.InternalServerException;
import infrastructure.server.exceptions.WebApplicationException;
import org.junit.Test;

import java.net.HttpURLConnection;

import static infrastructure.server.assertions.HttpContextAssert.assertThat;


public class ErrorHandlerTest {

    @Test
    public void IllegalArgumentException_should_be_handled(){
        //Given
        HttpContext httpContext = MockHttpContext.builder().build();
        IllegalArgumentException exception = new IllegalArgumentException("exception");

        //When
        ErrorHandler.handleError(exception, httpContext);

        //Then
        assertThat(httpContext)
                .hasResponseBody(new Error(HttpURLConnection.HTTP_BAD_REQUEST, exception.getMessage()));

    }


    @Test
    public void WebApplicationException_should_be_handled(){
        //Given
        HttpContext httpContext = MockHttpContext.builder().build();
        WebApplicationException exception = new InternalServerException(new RuntimeException("message"));

        //When
        ErrorHandler.handleError(exception, httpContext);

        //Then
        assertThat(httpContext)
                .hasResponseBody(new Error(HttpURLConnection.HTTP_INTERNAL_ERROR, exception.getMessage()));

    }


    @Test
    public void unknown_exceptions_should_be_handled(){
        //Given
        HttpContext httpContext = MockHttpContext.builder().build();
        RuntimeException exception = new RuntimeException("message");

        //When
        ErrorHandler.handleError(exception, httpContext);

        //Then
        assertThat(httpContext)
                .hasResponseBody(new Error(HttpURLConnection.HTTP_INTERNAL_ERROR, exception.getMessage()));

    }
}