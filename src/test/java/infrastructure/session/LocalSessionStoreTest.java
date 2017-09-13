package infrastructure.session;

import domain.model.user.User;
import domain.model.user.UserBuilder;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class LocalSessionStoreTest {

    private final LocalSessionStore sessionStore = LocalSessionStore.instance();


    @Test
    public void should_exist_just_one_instance(){
        //When
        LocalSessionStore instance1 = LocalSessionStore.instance();
        LocalSessionStore instance2 = LocalSessionStore.instance();

        //Then
        assertThat(instance1)
                .isSameAs(instance2);
    }

    @Test
    public void should_create_session(){
        //Given
        User user = UserBuilder.anUser();

        //When
        Session session = sessionStore.createSession(user);

        //Then
        assertThat(session)
                .isNotNull();
    }



    @Test
    public void get_user_session_returns_the_valid_user(){
        //Given: A user with session
        User user = UserBuilder.anUser();
        Session session = sessionStore.createSession(user);

        //When: we retrieve the user of the session
        Optional<User> retrievedUserId = sessionStore.userOf(session.getId());

        //Then: the expected user is retrieved
        assertThat(retrievedUserId).isPresent()
                .contains(user);
    }

    @Test
    public void a_user_with_expired_session_should_not_be_found() throws Exception{
        //Given:
        // A Local storage with an expiration time of 1 ms and the automatic expiration disabled
        LocalSessionStore sessionStore = new LocalSessionStore(1, false);
        //A user with session
        User user = UserBuilder.anUser();
        String sessionId = sessionStore.createSession(user).getId();
        //And the expiration time reached
        Thread.sleep(2);

        //When: we retrieve the user of the session
        Optional<User> retrievedUserId = sessionStore.userOf(sessionId);

        //Then: the session has been expired
        assertThat(retrievedUserId)
                .as("the session is expired so it shouldn't be retrieved")
                .isNotPresent();
    }

    @Test
    public void sessions_should_be_expired_automatically() throws Exception{
        //Given:
        // A Local storage with an expiration time of 1 ms
        LocalSessionStore sessionStore = new LocalSessionStore(1);
        Session newSession = sessionStore.createSession(UserBuilder.anUser());
        //And the expiration time reached
        Thread.sleep(200);

        //When: we check if the session exists in the store
        Optional<User> session = sessionStore.userOf(newSession.getId());

        //Then: the session has been expired
        assertThat(session)
                .as("the session should be expired")
                .isNotPresent();
    }


    @Test
    public void sessions_accessed_should_not_be_expired() throws Exception{
        //Given:
        // A Local storage with an expiration time of 200 ms
        LocalSessionStore sessionStore = new LocalSessionStore(200, false);
        Session session = sessionStore.createSession(UserBuilder.anUser());
        //And the expiration time reached but with an access before reaching the expiration time
        Thread.sleep(50);
        sessionStore.userOf(session.getId());
        Thread.sleep(150);

        //When: we retrieve the user of the session
        Optional<User> retrievedUserId = sessionStore.userOf(session.getId());

        //Then: the session has not been expired
        assertThat(retrievedUserId)
                .as("the session shouldn't be expired")
                .isPresent();

    }

    @Test
    public void should_delete_session(){
        //Given: a session
        Session session = sessionStore.createSession(UserBuilder.anUser());

        //When: the session is removed
        sessionStore.deleteSession(session.getId());

        //Then
        assertThat(sessionStore.userOf(session.getId()))
                .isEmpty();
    }
}
