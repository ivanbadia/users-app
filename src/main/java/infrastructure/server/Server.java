package infrastructure.server;


/**
 * Interface to be implemented by the http server
 */
public interface Server {

    /**
     * Starts the server
     *
     */
    void start();

    /**
     * Stops the server
     */
    void stop();

}
