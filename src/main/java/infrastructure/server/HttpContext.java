package infrastructure.server;


import domain.model.user.User;
import infrastructure.session.Session;

import java.util.Optional;

/**
 * Represents the context for the handling of an HTTP request
 * <p>
 * The context provides access to the request by {@link HttpRequest} and the response by {@link HttpResponse}
 * <p>
 * This interface has been created to control the HTTP request without using {@link com.sun.net.httpserver.HttpExchange}
 * <p>
 * I manage to keep the boundaries with {@link com.sun.net.httpserver.HttpExchange} using this interface and I make sure that a future change
 * of http server implementation wouldn't be too costly.
 */
public interface HttpContext {

    HttpResponse response();

    HttpRequest request();

    HttpContext fail(Error error);

    void setUser(User user);

    User user();

    void setSession(Session session);

    Optional<String> sessionId();
}
