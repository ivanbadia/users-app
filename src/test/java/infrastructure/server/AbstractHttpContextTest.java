package infrastructure.server;

import domain.model.user.UserBuilder;
import infrastructure.session.Session;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.Optional;

import static infrastructure.server.assertions.HttpContextAssert.assertThat;


public class AbstractHttpContextTest {
    @Test
    public void session_should_be_added_as_cookie(){
        //Given
        Session session = new Session("id", UserBuilder.anUser(), 1000);
        HttpContext httpContext = MockHttpContext.builder().build();

        //When
        httpContext.setSession(session);

        //Then
        assertThat(httpContext).hasHeader("Set-Cookie",  new SessionCookie("id").asHeaderValue()+"; HttpOnly");

    }

    @Test
    public void session_id_should_not_be_retrieved_if_there_is_no_session_cookie(){
        //Given
        HttpContext httpContext = MockHttpContext.builder()
                .withHeader("Cookie", "X=value; HttpOnly")
                .build();

        //When
        Optional<String> sessionId = httpContext.sessionId();

        //Then
        Assertions.assertThat(sessionId).isNotPresent();

    }


    @Test
    public void session_id_should_be_retrieved_from_cookie(){
        //Given
        HttpContext httpContext = MockHttpContext.builder()
                                        .withHeader("Cookie", new SessionCookie("id").asHeaderValue())
                                        .build();

        //When
        Optional<String> sessionId = httpContext.sessionId();

        //Then
        Assertions.assertThat(sessionId).isPresent().contains("id");

    }

    @Test
    public void error_should_be_added_to_response()  {

        //Given
        HttpContext httpContext = MockHttpContext.builder()
                    .build();
        Error error = new Error(HttpURLConnection.HTTP_UNAUTHORIZED, "error message");

        //When
        httpContext.fail(error);

        //Then
        assertThat(httpContext)
                .hasStatusCode(error.getStatusCode())
                .hasResponseBody(error);
    }
}