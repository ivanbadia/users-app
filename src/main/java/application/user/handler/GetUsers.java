package application.user.handler;


import application.user.representation.UserRepresentation;
import application.user.representation.UserRepresentationList;
import domain.model.user.UserRepository;
import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import io.reactivex.Maybe;

import java.util.List;
import java.util.stream.Collectors;

public class GetUsers implements Handler<UserRepresentationList> {
    private final UserRepository userRepository;

    public GetUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Maybe<UserRepresentationList> handle(HttpContext httpContext) {
        List<UserRepresentation> users = userRepository.all()
                                                .stream()
                                                .map(UserRepresentation::from)
                                                .collect(Collectors.toList());
        return Maybe.just(new UserRepresentationList(users));
    }
}
