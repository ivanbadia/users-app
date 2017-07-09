package infrastructure.session;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class SessionIdGeneratorTest {

    @Test
    public void generates_session_id(){
        //When
        String id = SessionIdGenerator.generateSessionId();

        //Then
        assertThat(id)
                .isNotNull();
    }

    @Test
    public void should_generates_different_session_ids(){

        //When
        String id = SessionIdGenerator.generateSessionId();
        String id2 = SessionIdGenerator.generateSessionId();

        //Then
        assertThat(id).isNotEqualTo(id2);
    }
}
