package infrastructure.server;


import domain.model.user.Role;

public interface RouteBuilder {


    /**
     * Specifies the request handler for the route
     *
     * @param handler
     * @return
     */
    RouteBuilder handler(Handler handler);

    /**
     * Adds a required authority for this route
     *
     * @param role
     * @return
     */
    RouteBuilder addAuthority(Role role);

    /**
     * Specifies the MIME media types that a route can produce and send back to the client
     *
     * @param contentType
     * @return
     */
    RouteBuilder produces(ContentType contentType);


}
