package infrastructure.session;


import domain.model.user.User;

import java.util.Optional;

/**
 *  Session store used to store sessions for the web application
 */
public interface SessionStore {

    Session createSession(User user);

    Optional<User> userOf(String sessionId);

    void deleteSession(String sessionId);
}
