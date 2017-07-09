package infrastructure.server.exceptions;


import java.net.HttpURLConnection;

public class NotFoundException extends WebApplicationException {

    public NotFoundException(String message) {
        super(HttpURLConnection.HTTP_NOT_FOUND, message);
   }

}
