package infrastructure.server;

import java.io.InputStream;
import java.util.Optional;

/**
 * Represents an HTTP request
 * <p>
 *  An instance is created and associated to every {@link HttpContext}
 * <p>
 * This interface has been created to control the HTTP request without using {@link com.sun.net.httpserver.HttpExchange}
 * <p>
 * I manage to keep the boundaries with {@link com.sun.net.httpserver.HttpExchange} using this interface and I make sure that
 * a future change of http server implementation wouldn't be too costly.
 */
public interface HttpRequest {

    Optional<String> pathParameter(String name);

    HttpRequest setCurrentRoute(Route route);

    String path();

    HttpMethod httpMethod();

    <T> Optional<T> body(Class<T> classOfT);

    InputStream body();

    Optional<ContentType> contentType();

    Optional<String> header(String header);

    Optional<String> parameter(String name);
}
