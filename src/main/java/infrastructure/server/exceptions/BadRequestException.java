package infrastructure.server.exceptions;


import java.net.HttpURLConnection;

public class BadRequestException extends WebApplicationException {

    public BadRequestException(String message) {
        super(HttpURLConnection.HTTP_BAD_REQUEST, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(HttpURLConnection.HTTP_BAD_REQUEST, message, cause);
   }

}
