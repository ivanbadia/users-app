package infrastructure.server.exceptions;


public class ServerInitializationException extends RuntimeException {

    public ServerInitializationException(Throwable cause) {
        super("Error creating HttpServer", cause);
    }
}
