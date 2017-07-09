package application.web.handler;


import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import infrastructure.session.SessionStore;
import io.reactivex.Maybe;

public class LogoutUser implements Handler{

    private final String loginPage;
    private final SessionStore sessionStore;

    public LogoutUser(SessionStore sessionStore, String loginPage) {
        this.loginPage = loginPage;
        this.sessionStore = sessionStore;
    }

    @Override
    public Maybe handle(HttpContext httpContext) {
        httpContext.sessionId()
                .ifPresent(sessionStore::deleteSession);
        httpContext.response().redirect(loginPage);

        return Maybe.empty();
    }
}
