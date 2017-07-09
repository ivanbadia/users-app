package infrastructure.server.exceptions;


import java.net.HttpURLConnection;

public class UnauthorizedException  extends WebApplicationException {
    public UnauthorizedException(String message) {
        super(HttpURLConnection.HTTP_UNAUTHORIZED, message);
    }
}
