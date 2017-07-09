package infrastructure.server;

import io.reactivex.Maybe;

/**
 * A handler is responsible for processing an HTTP request and it`s assigned to routes
 */
@FunctionalInterface
public interface Handler<T> {

    Maybe<T> handle(HttpContext httpContext);
}
