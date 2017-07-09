package infrastructure.server;


import domain.model.user.Role;
import domain.model.user.User;
import infrastructure.server.exceptions.AccessDeniedException;
import io.reactivex.Maybe;

import java.util.Map;

/**
 * A route is an http path and http method that determine if an HTTP request should be routed
 * to a handler. When a request is made, the matching route handler is invoked.
 */
public class Route implements RouteBuilder{

    private Handler handler;
    private final HttpMethod httpMethod;
    private final RoutePath routePath;
    private ContentType produces;
    private Role role;


    Route(HttpMethod httpMethod, String path){
        this.httpMethod = httpMethod;
        this.routePath = new RoutePath(path);
    }

    Route(String path) {
        this(null, path);
    }



    @Override
    public RouteBuilder handler(Handler handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public RouteBuilder addAuthority(Role role) {
        this.role = role;
        return this;
    }

    @Override
    public RouteBuilder produces(ContentType contentType) {
        this.produces = contentType;
        return this;
    }


    Maybe handleContext(HttpContext context){
        if(produces!=null){
            context.response().setContentType(produces);
        }

        if(role!=null) {
            User user = context.user();
            if (user==null || !user.isAuthorized(role)) {
                return Maybe.error(new AccessDeniedException("User hasn't got the authority " + role));
            }
        }

        return handler.handle(context);
    }


    public boolean isRouteFor(HttpMethod httpMethod, String uri) {
        return httpMethodMatches(httpMethod) && routePath.matches(uri);
    }

    private boolean httpMethodMatches(HttpMethod httpMethod) {
        return this.httpMethod==null || this.httpMethod.equals(httpMethod);
    }

    public Map<String, String> pathParamsOf(String path) {
        return routePath.pathParamsOf(path);
    }
}
