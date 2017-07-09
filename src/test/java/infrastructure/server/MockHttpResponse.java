package infrastructure.server;

import java.util.Optional;

public class MockHttpResponse extends AbstractHttpResponse implements HttpResponse {

    private boolean ended;
    private String redirectedTo;

    @Override
    public void end() {
        this.ended = true;
    }

    @Override
    public boolean isEnded() {
        return ended;
    }

    @Override
    public void redirect(String uri) {
        this.redirectedTo = uri;
    }

    public String getRedirectedTo() {
        return redirectedTo;
    }

    public Optional<Object> getBody() {
        return Optional.ofNullable(body);
    }
}
