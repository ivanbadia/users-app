package infrastructure.server.httpserver;

import com.sun.net.httpserver.HttpExchange;
import infrastructure.server.AbstractHttpRequest;
import infrastructure.server.ContentType;
import infrastructure.server.ContentTypeParser;
import infrastructure.server.HttpMethod;
import infrastructure.server.constants.Headers;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static infrastructure.server.ContentType.FORM_URL_ENCODED;

/**
 * Decorates HttpExchange to provide an implementation of {@link infrastructure.server.HttpRequest}
 */
public class HttpExchangeRequest extends AbstractHttpRequest{
    private static final Logger LOGGER = Logger.getLogger(HttpExchangeRequest.class.getName());

    private final HttpExchange httpExchange;
    private final Map<String, String> parameters;
    private ContentType contentType;

    public static HttpExchangeRequest from(HttpExchange httpExchange){
        return new HttpExchangeRequest(httpExchange);
    }

    private HttpExchangeRequest(HttpExchange httpExchange) {
        super(HttpMethod.valueOf(httpExchange.getRequestMethod()), httpExchange.getRequestURI().getPath());
        this.httpExchange = httpExchange;
        this.contentType = header(Headers.CONTENT_TYPE)
                .flatMap(ContentTypeParser::extractContentTypeFrom)
                .orElse(null);
        this.parameters = parsePostParameters();
    }

    @Override
    public InputStream body() {
        return httpExchange.getRequestBody();
    }

    @Override
    public Optional<ContentType> contentType() {
        return Optional.ofNullable(this.contentType);
    }


    @Override
    public Optional<String> header(String header) {
        return Optional.ofNullable(httpExchange.getRequestHeaders())
                .map(headers -> headers.getFirst(header));
    }

    @Override
    public Optional<String> parameter(String name) {
        return Optional.ofNullable(parameters.get(name));
    }


    /**
     * Parses the parameters of the POST request with content-type application/x-www-form-urlencoded
     */
    private Map<String, String> parsePostParameters() {
        Map<String, String> parameters = null;
        if(isPostWithFormData()) {
            InputStream requestBody = httpExchange.getRequestBody();
            BufferedReader bufferedReader = bufferReaderOf(requestBody);
            parameters = parseParameters(bufferedReader);
        }

        return Optional.ofNullable(parameters).orElse(new HashMap<>());
    }

    private Map<String, String> parseParameters(BufferedReader br) {
        Map<String, String> parameters;
        String query;
        try {
            query = br.readLine();
            parameters = parseQuery(query);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error parsing body", e);
            parameters = new HashMap<>();
        }
        return parameters;
    }

    private BufferedReader bufferReaderOf(InputStream requestBody) {
        InputStreamReader isr =
                new InputStreamReader(requestBody, StandardCharsets.UTF_8);
        return new BufferedReader(isr);
    }

    private boolean isPostWithFormData() {
        return httpMethod().equals(HttpMethod.POST) && contentType!=null && contentType.equals(FORM_URL_ENCODED);
    }


    private static Map<String, String> parseQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> parameters = new HashMap<>();
        if (query != null) {
            String pairs[] = query.split("&");
            for (String pair : pairs) {
                String param[] = pair.split("=");

                if (param.length > 0) {
                    int paramIndex = 0;
                    String key = URLDecoder.decode(param[paramIndex++], StandardCharsets.UTF_8.name());
                    String value = "";
                    if (param.length > 1) {
                        value = URLDecoder.decode(param[paramIndex], StandardCharsets.UTF_8.name());
                    }
                    parameters.put(key, value);
                }
            }
        }

        return parameters;
    }
}
