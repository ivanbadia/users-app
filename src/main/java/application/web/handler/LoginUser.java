package application.web.handler;

import domain.model.user.AuthenticationService;
import domain.model.user.User;
import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import infrastructure.session.Session;
import infrastructure.session.SessionStore;
import io.reactivex.Maybe;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


public class LoginUser implements Handler<String> {

    private static final String USERNAME_PARAMETER = "username";
    private  static final String PASSWORD_PARAMETER = "password";
    private  static final String FROM_PARAMETER = "from";

    private String homePage;
    private SessionStore sessionStore;
    private AuthenticationService authenticationService;
    private LoginPageRender loginPageRender;


    private static final Predicate<String> isNotEmpty = not(String::isEmpty);

    private Function<User, Session> generateSession = user -> sessionStore.createSession(user);

    private Maybe<String> redirectTo(HttpContext context, String path) {
        context.response().redirect(path);
        return Maybe.empty();
    }

    public LoginUser(SessionStore sessionStore, AuthenticationService authenticationService, LoginPageRender loginPageRender, String homePage) {
        this.sessionStore = sessionStore;
        this.authenticationService = authenticationService;
        this.loginPageRender = loginPageRender;
        this.homePage = homePage;
    }

    @Override
    public Maybe<String> handle(HttpContext httpContext) {
        Optional<String> username = httpContext.request().parameter(USERNAME_PARAMETER)
                                        .filter(isNotEmpty);

        if (!username.isPresent()) {
           return renderLoginPageWithError(httpContext, "The username is required");
        }

        Optional<String> password = httpContext.request().parameter(PASSWORD_PARAMETER)
                                        .filter(isNotEmpty);

        if (!password.isPresent()) {
            return renderLoginPageWithError(httpContext, "The password is required");
        }

        return authenticationService.authenticate(username.get(), password.get())
                .map(generateSession)
                .map(setSessionToHttpContext(httpContext))
                .map(redirectToNextPage())
                .orElseGet(() -> renderLoginPageWithError(httpContext, "The credentials are not valid"));
    }

    private Function<Session, HttpContext> setSessionToHttpContext(HttpContext httpContext) {
        return session -> {
            httpContext.setSession(session);
            return httpContext;
        };
    }

    private  Function<HttpContext, Maybe<String>> redirectToNextPage() {
        return context -> context.request().parameter("from")
                        .map(from -> redirectTo(context, from))
                        .orElseGet(() -> redirectTo(context, homePage));
    }

    private static Predicate<String> not(Predicate<String> isEmpty) {
        return isEmpty.negate();
    }

    private Maybe<String> renderLoginPageWithError(HttpContext httpContext, String errorMessage) {
        Map<String, Object> scopes = new HashMap<>();
        httpContext.request().parameter(FROM_PARAMETER)
                .ifPresent(fromParam -> scopes.put(LoginPageRender.FROM_VARIABLE, fromParam));
        scopes.put(LoginPageRender.ERROR_MESSAGE_VARIABLE, errorMessage);
        scopes.put(LoginPageRender.USERNAME_VARIABLE, httpContext.request().parameter(USERNAME_PARAMETER).orElse(""));
        scopes.put(LoginPageRender.PASSWORD_VARIABLE, httpContext.request().parameter(PASSWORD_PARAMETER).orElse(""));

        return loginPageRender.render(scopes);
    }



}
