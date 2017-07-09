package infrastructure.server;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that provide the methods to define the routes {@link Route}
 */
public final class Routes {

    private final List<Route> filters = new ArrayList<>();
    private final List<Route> routes = new ArrayList<>();

    private static class RoutesHolder {
        private static final Routes INSTANCE = new Routes();
    }

    private Routes(){}

    static Routes instance() {
        return RoutesHolder.INSTANCE;
    }

    public static RouteBuilder get(String path) {
        return instance().addRoute(HttpMethod.GET, path);
    }

    public static RouteBuilder put(String path) {
        return instance().addRoute(HttpMethod.PUT, path);
    }

    public static RouteBuilder delete(String path) {
        return instance().addRoute(HttpMethod.DELETE, path);
    }

    public static RouteBuilder post(String path) {
        return instance().addRoute(HttpMethod.POST, path);
    }

    public static RouteBuilder filter(String path) {
        return instance().addFilter(path);
    }

    void clearAll(){
        filters.clear();
        routes.clear();
    }

    List<Route> routes(){
        return Collections.unmodifiableList(routes);
    }

    List<Route> filters(){
        return Collections.unmodifiableList(filters);
    }

    private RouteBuilder addFilter(String path) {
        Route route = new Route(path);
        filters.add(route);
        return route;
    }


    private Route addRoute(HttpMethod method, String path) {
        Route route = new Route(method, path);
        routes.add(route);
        return route;
    }

}
