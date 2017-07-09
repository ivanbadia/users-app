package infrastructure.server.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import infrastructure.server.AbstractHttpResponse;
import infrastructure.server.ContentType;
import infrastructure.server.HttpResponse;
import infrastructure.server.bodywriters.BodyWriterProvider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import static infrastructure.server.ContentTypeParser.*;
import static infrastructure.server.constants.Headers.*;

/**
 * Decorates HttpExchange to provide an implementation of {@link infrastructure.server.HttpResponse}
 */
public class HttpExchangeResponse extends AbstractHttpResponse{

    private static final Logger LOGGER = Logger.getLogger(HttpExchangeResponse.class.getName());

    private final HttpExchange httpExchange;
    private boolean ended = false;


    private HttpExchangeResponse(HttpExchange exchange){
        this.httpExchange = exchange;
        this.setContentType(responseContentType());
    }


    public static HttpResponse from(HttpExchange exchange){
        return new HttpExchangeResponse(exchange);
    }



    @Override
    public void end() {
        synchronized(httpExchange) {
            if (!ended) {
                try {
                    writeHeaders();
                    writeBody();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error writing response", e);
                } finally {
                    close();
                }
                ended = true;
            }

        }
    }

    private void writeBody() {
        if(this.body!=null) {
            BodyWriterProvider.instance()
                    .bodyWriterFor(contentType().orElse(""))
                    .write(this.body, httpExchange.getResponseBody());
        }
    }

    private void writeHeaders() throws IOException {
        Headers responseHeaders = httpExchange.getResponseHeaders();
        if (responseHeaders != null) {
            headers().entrySet().forEach(entry -> responseHeaders.add(entry.getKey(), entry.getValue()));
        }
        httpExchange.sendResponseHeaders(statusCode(), 0);
    }

    private void close() {
        try {
            httpExchange.getResponseBody().close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing response", e);
        }
        httpExchange.close();
    }


    @Override
    public boolean isEnded() {
        return ended;
    }

    @Override
    public void redirect(String uri) {
        httpExchange.getResponseHeaders().set("Location", uri);
        setStatusCode(HttpURLConnection.HTTP_MOVED_TEMP);
    }


    private ContentType responseContentType() {
        ContentType acceptedContentType = null;
        if(httpExchange.getRequestHeaders()!=null) {
            acceptedContentType = extractAcceptedContentTypeFrom(requestHeader(ACCEPT))
                                        .orElse(extractContentTypeFrom(requestHeader(CONTENT_TYPE)).orElse(null));
        }

        return acceptedContentType;


    }

    private String requestHeader(String contentType) {
        return httpExchange.getRequestHeaders().getFirst(contentType);
    }


}
