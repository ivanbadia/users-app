package infrastructure.server.httpserver;

import com.sun.net.httpserver.HttpServer;
import infrastructure.server.HttpContext;
import infrastructure.server.Router;
import infrastructure.server.exceptions.ServerInitializationException;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Implementation of {@link infrastructure.server.Server} using {@link com.sun.net.httpserver.HttpServer}
 */
public class SunHttpServer implements infrastructure.server.Server {
    private static final int PORT = 8080;
    private HttpServer server;
    private ExecutorService executor;


    @Override
    public void start() {
        executor = Executors.newCachedThreadPool();

        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);

            server.createContext("/", httpExchange -> {
                HttpContext context = HttpExchangeContext.from(httpExchange);
                Router.instance().handle(context)
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            });

            server.setExecutor(executor);

            server.start();
        } catch (IOException e) {
            throw new ServerInitializationException(e);
        }


    }


    @Override
    public void stop() {
        server.stop(0);
        executor.shutdownNow();
    }

}
