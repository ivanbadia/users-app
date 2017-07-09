package application.auth.handler;

import application.web.handler.LoginPageRender;
import domain.model.user.User;
import infrastructure.server.ContentType;
import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import infrastructure.session.SessionStore;
import io.reactivex.Maybe;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;


public class SessionAuthFilter implements Handler<String> {

    private final SessionStore sessionStore;
    private final LoginPageRender loginPageRender;


    public SessionAuthFilter(SessionStore sessionStore, LoginPageRender loginPageRender) {
        this.sessionStore = sessionStore;
        this.loginPageRender = loginPageRender;
    }

    @Override
    public Maybe<String> handle(HttpContext httpContext) {
        return httpContext.sessionId()
                .flatMap(sessionStore::userOf)
                .map(setAuthenticatedUserToContext(httpContext))
                .orElseGet(redirectToLoginPage(httpContext));
    }

    private Supplier<Maybe<String>> redirectToLoginPage(HttpContext httpContext) {
        return () -> {
            httpContext.response().setContentType(ContentType.HTML);
            Map<String, Object> scopes = new HashMap<>();
            scopes.put(LoginPageRender.FROM_VARIABLE, httpContext.request().path());
            return loginPageRender.render(scopes);
        };
    }

    private Function<User, Maybe<String>> setAuthenticatedUserToContext(HttpContext httpContext) {
        return user -> {
            httpContext.setUser(user);
            return Maybe.empty();
        };
    }
}
