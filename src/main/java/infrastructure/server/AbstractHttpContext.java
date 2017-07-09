package infrastructure.server;


import domain.model.user.User;
import infrastructure.server.constants.Headers;
import infrastructure.session.Session;

import java.util.Optional;

/**
 * Represents the context for the handling of an HTTP request
 * <p>
 * The context provides access to the request by {@link HttpRequest} and the response by {@link HttpResponse}
 * <p>
 * This class has been created to control the HTTP request without using {@link com.sun.net.httpserver.HttpExchange}
 * <p>
 * I manage to keep the boundaries with {@link com.sun.net.httpserver.HttpExchange} using this interface and I make sure that a future change
 * of http server implementation wouldn't be too costly.
 */
public class AbstractHttpContext implements HttpContext{

    private final HttpRequest request;
    private final HttpResponse response;
    private User user;


    protected AbstractHttpContext(HttpRequest request, HttpResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpResponse response(){
        return this.response;
    }

    public HttpRequest request(){
        return this.request;
    }

    public AbstractHttpContext fail(Error error) {
        response()
                .setStatusCode(error.getStatusCode())
                .setBody(error)
                .end();
        return this;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User user(){
       return this.user;
    }


    public void setSession(Session session){
       response().addCookie(new SessionCookie(session.getId()).toHttpCookie());
    }


    public Optional<String> sessionId(){
        return request().header(Headers.COOKIE)
                .flatMap(SessionCookie::fromHeader)
                .map(SessionCookie::getValue);
    }
}
