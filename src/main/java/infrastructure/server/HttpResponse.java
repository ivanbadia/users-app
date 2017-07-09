package infrastructure.server;

import java.net.HttpCookie;
import java.util.Optional;

/**
 * Represents an HTTP response
 * <p>
 *  An instance is created and associated to every {@link HttpContext}
 * <p>
 * This interface has been created to control the HTTP response without using {@link com.sun.net.httpserver.HttpExchange}
 * <p>
 * I manage to keep the boundaries with {@link com.sun.net.httpserver.HttpExchange} using this interface and I make sure that
 * a future change of http server implementation wouldn't be too costly.
 */
public interface HttpResponse {

    HttpResponse setStatusCode(int statusCode);

    HttpResponse setBody(Object body);

    Optional<String> header(String header);

    HttpResponse setContentType(ContentType contentType);

    void end();

    HttpResponse addHeader(String name, String value);

    HttpResponse addCookie(HttpCookie cookie);

    Integer statusCode();

    boolean isEnded();

    Optional<String> contentType();

    void redirect(String uri);

    boolean hasBody();
}
