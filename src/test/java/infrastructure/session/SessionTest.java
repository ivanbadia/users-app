package infrastructure.session;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class SessionTest {

    @Test
    public void session_should_not_be_expired()  {
        //Given
        Session session = new Session("sessionId", null ,10000 );

        //When
        boolean expired = session.isExpired();

        //Then
        assertThat(expired).isFalse();
    }

    @Test
    public void session_should_be_expired() throws InterruptedException {
        //Given
        Session session = new Session("sessionId", null, 0 );

        //When
        Thread.sleep(1);
        boolean expired = session.isExpired();

        //Then
        assertThat(expired).isTrue();

    }
}