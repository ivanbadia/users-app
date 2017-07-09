package infrastructure.server;

import infrastructure.server.exceptions.WebApplicationException;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;


class ErrorHandler {

    public static void handleError(Throwable throwable, HttpContext context) {
        Error error;
        if (throwable instanceof IllegalArgumentException) {
            error = new Error(HTTP_BAD_REQUEST, throwable.getMessage());
        } else if (throwable instanceof WebApplicationException) {
            WebApplicationException exception = (WebApplicationException) throwable;
            error = new Error(exception.getStatusCode(), exception.getMessage());
        } else {
            error = new Error(HTTP_INTERNAL_ERROR, throwable.getMessage());
        }

        context.fail(error);

    }
}
