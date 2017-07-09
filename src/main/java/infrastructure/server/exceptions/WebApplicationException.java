package infrastructure.server.exceptions;

public class WebApplicationException extends RuntimeException {

    private final int statusCode;

    WebApplicationException(int statusCode, Throwable cause) {
        this(statusCode, null, cause);
    }

    WebApplicationException(int statusCode, String message) {
        this(statusCode, message, null);
    }

    WebApplicationException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
