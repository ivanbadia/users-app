package application.auth.handler;

import domain.model.user.AuthenticationService;
import domain.model.user.User;
import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import infrastructure.server.constants.Headers;
import infrastructure.server.exceptions.UnauthorizedException;
import io.reactivex.Maybe;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;


public class BasicAuthFilter implements Handler{

    private AuthenticationService authenticationService;

    private final static Maybe<Object> unauthorizedError = Maybe.error(new UnauthorizedException("User credentials are not valid"));

    private final Function<String, String[]> extractCredentialsFromHeader = header -> {
        String packet = header.substring("Basic".length()).trim();
        //The content of the header is username:password in Base64
        return new String(Base64.decodeBase64(packet), StandardCharsets.UTF_8).split(":", 2);
    };

    private final Function<String[], Optional<User>> authenticateUser = values -> {
        final String username = values[0];
        final String password = values[1];
        return authenticationService.authenticate(username, password);
    };

    public BasicAuthFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Maybe handle(HttpContext httpContext) {
        return httpContext.request().header(Headers.AUTHORIZATION)
                .filter(header -> header.startsWith("Basic"))
                .map(extractCredentialsFromHeader)
                .flatMap(authenticateUser)
                .map(setAuthenticatedUserToContext(httpContext))
                .orElse(unauthorizedError);

    }

    private Function<User, Maybe<Object>> setAuthenticatedUserToContext(HttpContext httpContext) {
        return user -> {
            httpContext.setUser(user);
            return Maybe.empty();
        };
    }


}
