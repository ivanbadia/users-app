package infrastructure.server;


import domain.model.user.User;

public class MockHttpContext extends AbstractHttpContext {

    private MockHttpContext(HttpRequest request, HttpResponse response) {
        super(request, response);
    }
    public static MockHttpContextBuilder builder(){
        return new MockHttpContextBuilder();
    }


    public static class MockHttpContextBuilder {

        private final MockHttpRequest.MockRequestBuilder httpRequestBuilder = MockHttpRequest.builder();
        private User user;

        public MockHttpContextBuilder withHttpMethod(HttpMethod httpMethod) {
            httpRequestBuilder.withHttpMethod(httpMethod);
            return this;
        }

        public MockHttpContextBuilder withPath(String path) {
            httpRequestBuilder.withPath(path);
            return this;
        }

        public MockHttpContextBuilder withHeader(String header, String value) {
            httpRequestBuilder.withHeader(header, value);
            return this;
        }

        public MockHttpContextBuilder withParameter(String name, String value) {
            httpRequestBuilder.withParameter(name, value);
            return this;
        }

        public MockHttpContextBuilder withRoute(RouteBuilder route) {
            httpRequestBuilder.withRoute(route);
            return this;
        }

        public MockHttpContextBuilder withContentType(String contentType) {
            httpRequestBuilder.withContentType(contentType);
            return this;
        }


        public HttpContext build() {
            HttpContext httpContext = new MockHttpContext(httpRequestBuilder.build(), new MockHttpResponse());
            httpContext.setUser(user);
            return httpContext;
        }

        public MockHttpContextBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public MockHttpContextBuilder withBody(String body) {
            httpRequestBuilder.withBody(body);
            return this;
        }

        public MockHttpContextBuilder withSessionId(String sessionId) {
            httpRequestBuilder.withHeader("Cookie", new SessionCookie(sessionId).asHeaderValue());
            return this;
        }
    }
}
