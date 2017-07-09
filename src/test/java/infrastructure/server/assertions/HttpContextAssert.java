package infrastructure.server.assertions;

import domain.model.user.User;
import infrastructure.server.HttpContext;
import infrastructure.server.MockHttpResponse;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Objects;

public class HttpContextAssert extends AbstractAssert<HttpContextAssert, HttpContext> {

    private HttpContextAssert(HttpContext actual) {
        super(actual, HttpContextAssert.class);
    }


    public static HttpContextAssert assertThat(HttpContext context) {
        return new HttpContextAssert(context);
    }

    public HttpContextAssert hasStatusCode(int statusCode) {
        contextAndResponseAreNotNull();

        if (!Objects.equals(actual.response().statusCode(), statusCode)) {
            failWithMessage("Expected status code to be <%s> but was <%s>", statusCode, actual.response().statusCode());
        }

        return this;
    }

    private void contextAndResponseAreNotNull() {
        isNotNull();

        Assertions.assertThat(actual.response()).isNotNull();
    }

    public HttpContextAssert hasResponseBody(Object body) {
        contextAndResponseAreNotNull();

        MockHttpResponse response = (MockHttpResponse) actual.response();
        if (!response.getBody().isPresent() || !Objects.equals(response.getBody().get(), body)) {
            failWithMessage("Expected body to be <%s> but was <%s>", body, response.getBody().get());
        }

        return this;
    }

    public HttpContextAssert isEnded() {
        contextAndResponseAreNotNull();

        if (!actual.response().isEnded()) {
            failWithMessage("Expected response to be ended but it wasn't");
        }

        return this;
    }

    public HttpContextAssert hasUser(User user) {
        contextAndResponseAreNotNull();

        if (!Objects.equals(actual.user(), user)) {
            failWithMessage("Expected user to be <%s> but was <%s>", user, actual.user());
        }

        return this;
    }

    public HttpContextAssert hasContentType(String contentType) {
        contextAndResponseAreNotNull();

        if (!actual.response().contentType().isPresent() || !Objects.equals(actual.response().contentType().get(), contentType)) {
            failWithMessage("Expected content type to be <%s> but was <%s>", contentType, actual.response().contentType());
        }

        return this;
    }

    public HttpContextAssert hasHeader(String header, String value) {
        contextAndResponseAreNotNull();

        if (!actual.response().header(header).isPresent() || !actual.response().header(header).get().equals(value)) {
            failWithMessage("Expected header  to be <%s> but was <%s>", value, actual.response().header(header));
        }

        return this;
    }


    public HttpContextAssert wasRedirectedTo(String path) {
        contextAndResponseAreNotNull();

        MockHttpResponse response = (MockHttpResponse) actual.response();
        if (response.getRedirectedTo()==null || !response.getRedirectedTo().equals(path)) {
            failWithMessage("Expected response redirected to <%s> but was <%s>", path, ((MockHttpResponse) actual.response()).getRedirectedTo());
        }

        return this;
    }

    public HttpContextAssert containsHeader(String header) {
        contextAndResponseAreNotNull();

        if (!actual.response().header(header).isPresent()) {
            failWithMessage("Expected header <%s> to be present", header);
        }

        return this;
    }
}
