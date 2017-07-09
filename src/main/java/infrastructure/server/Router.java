package infrastructure.server;


import infrastructure.server.exceptions.NotFoundException;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import java.util.logging.Logger;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;


/**
 * The router is the component in charge of translating each incoming HTTP request to an action.
 *
 * The router receives requests from {@link infrastructure.server.httpserver.SunHttpServer} and routes them to the matching routes {@link Route}
 */
public class Router {
    private static final Logger LOGGER = Logger.getLogger(Router.class.getName());
    private final Observable<Route> routeNotFound =  Observable.error(new NotFoundException("path not found"));

    private static class RouterHolder {
        static final Router INSTANCE = new Router();
    }

    public static Router instance() {
        return RouterHolder.INSTANCE;
    }

    public Completable handle(HttpContext context) {


        Observable<Route> route = routeFor(context)
                .switchIfEmpty(routeNotFound)
                .map(assignRouteToRequest(context));

        Observable result = filtersFor(context)
                .concatWith(route)
                .flatMap(handleContext(context))
                //Just the first result is written to the body
                //If there are more results they will be ignored
                .firstElement()
                .map(writeResultToResponse(context))
                .toObservable();


        return Completable.fromObservable(result)
                .doOnSubscribe(disposable -> LOGGER.info("Executing request "+context.request().httpMethod()+" "+context.request().path()))
                .doOnComplete(endResponseOf(context))
                .doOnError(throwable -> ErrorHandler.handleError(throwable, context))
                .doFinally(() -> LOGGER.info("Executed request "+context.request().httpMethod()+" "+context.request().path()));

    }

    private Action endResponseOf(HttpContext context) {
        return () -> {
            setStatusCodeTo(context);
            context.response().end();
        };
    }

    private void setStatusCodeTo(HttpContext context) {
        if(context.response().statusCode()==null) {
            if (context.response().hasBody()) {
                context.response().setStatusCode(HTTP_OK);
            } else {
                context.response().setStatusCode(HTTP_NO_CONTENT);
            }
        }
    }

    private Function<Route, ObservableSource<Object>> handleContext(HttpContext context) {
        return route -> Observable.defer(()->route.handleContext(context).toObservable());
    }


    private Observable<Route> filtersFor(HttpContext context) {
        return Observable.fromIterable(Routes.instance().filters())
                .filter(isRouteFor(context));
    }


    private io.reactivex.functions.Function<Route, Route> assignRouteToRequest(HttpContext context) {
        return route -> {
            context.request().setCurrentRoute(route);
            return route;
        };
    }

    private io.reactivex.functions.Function writeResultToResponse(HttpContext context) {
        return result -> {
            context.response().setBody(result);
            return result;
        };
    }


    private Observable<Route> routeFor(HttpContext context) {
        return Observable.fromIterable(Routes.instance().routes())
                .filter(isRouteFor(context));
    }

    private Predicate<Route> isRouteFor(HttpContext context) {
        return route -> route.isRouteFor(context.request().httpMethod(), context.request().path());
    }




}
