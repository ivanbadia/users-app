package infrastructure.server.exceptions;


import java.net.HttpURLConnection;

public class InternalServerException extends  WebApplicationException {
    public InternalServerException(Throwable cause) {
        super(HttpURLConnection.HTTP_INTERNAL_ERROR, cause);
    }


    public InternalServerException(String message, Exception cause) {
        super(HttpURLConnection.HTTP_INTERNAL_ERROR, message, cause);
    }
}
