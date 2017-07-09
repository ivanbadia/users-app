package infrastructure.server.exceptions;


import java.net.HttpURLConnection;

public class AccessDeniedException extends WebApplicationException {
    public AccessDeniedException(String message) {
        super(HttpURLConnection.HTTP_FORBIDDEN, message);
    }
}
