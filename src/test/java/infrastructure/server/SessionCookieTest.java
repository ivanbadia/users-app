package infrastructure.server;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class SessionCookieTest {
    @Test
    public void cookie_should_be_created_from_header_value()  {
        //Given
        String header = "JSESSIONID=value";

        //When
        Optional<SessionCookie> sessionCookie = SessionCookie.fromHeader(header);

        //Then
        assertThat(sessionCookie)
                .isPresent()
                .matches(cookie -> cookie.get().getName().equals("JSESSIONID"))
                .matches(cookie ->  cookie.get().getValue().equals("value"));

    }


    @Test
    public void cookie_should_not_be_created_when_its_not_in_the_header()  {
        //Given
        String header = "x=b; z=x; c=d";

        //When
        Optional<SessionCookie> cookie = SessionCookie.fromHeader(header);

        //Then
        assertThat(cookie)
                .isNotPresent();

    }

}