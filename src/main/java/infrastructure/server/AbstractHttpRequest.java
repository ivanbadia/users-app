package infrastructure.server;


import infrastructure.server.bodyparsers.BodyParserProvider;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractHttpRequest implements HttpRequest {

    private final HttpMethod httpMethod;
    private final String path;
    private Map<String, String> pathParameters;

    protected AbstractHttpRequest(HttpMethod httpMethod, String path) {
        this.httpMethod = httpMethod;
        this.path = path;
    }


    @Override
    public HttpMethod httpMethod() {
        return httpMethod;
    }


    @Override
    public Optional<String> pathParameter(String name) {
        return Optional.ofNullable(pathParameters.get(name));
    }

    @Override
    public <T> Optional<T> body(Class<T> classOfT) {

        InputStream body = body();
        Optional<ContentType> contentType = contentType();
        if(body !=null && contentType.isPresent()) {
            return BodyParserProvider.instance().bodyParserFor(contentType.get())
                    .map(bodyParser -> bodyParser.parse(body, classOfT));
        }else{
            return Optional.empty();
        }

    }


    @Override
    public HttpRequest setCurrentRoute(Route route) {
        if(route!=null) {
            pathParameters = route.pathParamsOf(this.path);
        }
        return this;
    }

    @Override
    public String path() {
        return path;
    }

}
