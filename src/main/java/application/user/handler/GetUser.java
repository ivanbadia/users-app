package application.user.handler;


import application.user.representation.UserRepresentation;
import domain.model.user.UserRepository;
import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import infrastructure.server.exceptions.NotFoundException;
import io.reactivex.Maybe;

public class GetUser implements Handler<UserRepresentation> {
    private final UserRepository userRepository;

    private final Maybe<UserRepresentation> userNotFound = Maybe.error(new NotFoundException("User not found"));

    public GetUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Maybe<UserRepresentation> handle(HttpContext httpContext) {
        return httpContext.request().pathParameter("id")
                .flatMap(id -> userRepository.byId(Long.valueOf(id)))
                .map(UserRepresentation::from)
                .map(Maybe::just)
                .orElse(userNotFound);
    }
}
