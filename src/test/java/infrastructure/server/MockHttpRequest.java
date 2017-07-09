package infrastructure.server;

import infrastructure.server.constants.Headers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class MockHttpRequest extends AbstractHttpRequest {
    private final String body;
    private final Map<String, String> headers;
    private final Map<String, String> parameters;

    private MockHttpRequest(HttpMethod httpMethod, String path, String body, Map<String, String> headers, Map<String, String> parameters) {
        super(httpMethod, path);
        this.body = body;
        this.headers = headers;
        this.parameters = parameters;
    }



    public static MockRequestBuilder builder(){
        return new MockRequestBuilder();
    }


    @Override
    public InputStream body() {
        if(body!=null) {
            return new ByteArrayInputStream(body.getBytes());
        }else{
            return null;
        }
    }

    @Override
    public Optional<ContentType> contentType() {
        return ContentTypeParser.extractContentTypeFrom(headers.get(Headers.CONTENT_TYPE));
    }

    @Override
    public Optional<String> header(String header) {
        return Optional.ofNullable(headers.get(header));
    }

    @Override
    public Optional<String> parameter(String name) {
        return  Optional.ofNullable(parameters.get(name));
    }

    public static class MockRequestBuilder {
        private HttpMethod httpMethod;
        private String path;
        private String body;
        private final Map<String, String> headers = new HashMap<>();
        private final Map<String, String> parameters = new HashMap<>();
        private Route route;

        public MockRequestBuilder withHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public MockRequestBuilder withPath(String path) {
            this.path = path;
            return this;
        }

        public MockRequestBuilder withBody(String body) {
            this.body = body;
            return this;
        }

        public MockRequestBuilder withContentType(String contentType) {
            this.headers.put(Headers.CONTENT_TYPE, contentType);
            return this;
        }

        public MockRequestBuilder withHeader(String header, String value) {
            this.headers.put(header, value);
            return this;
        }

        public MockRequestBuilder withParameter(String name, String value) {
            this.parameters.put(name, value);
            return this;
        }

        public HttpRequest build() {
            return new MockHttpRequest(httpMethod, path, body, headers, parameters).setCurrentRoute(this.route);
        }


        public MockRequestBuilder withRoute(RouteBuilder routeBuilder) {
            this.route = (Route) routeBuilder;
            return this;
        }
    }
}
