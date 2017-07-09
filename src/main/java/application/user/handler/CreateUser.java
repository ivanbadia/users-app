package application.user.handler;

import application.user.command.CreateUserCommand;
import application.user.representation.UserRepresentation;
import domain.model.user.User;
import domain.model.user.UserRepository;
import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import infrastructure.server.exceptions.BadRequestException;
import io.reactivex.Maybe;

import java.util.function.Function;


public class CreateUser implements Handler<UserRepresentation>{

    private UserRepository userRepository;

    private static final Maybe<UserRepresentation> requestBodyIsNotValid = Maybe.error(new BadRequestException("The request body is not valid"));

    private final Function<User, User> saveUser = user -> {
        userRepository.add(user);
        return user;
    };
    private Function<CreateUserCommand, User> commandToUser = command -> new User(userRepository.nextId(), command.getUsername(), command.getPassword(), command.getRoles());

    public CreateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Maybe<UserRepresentation> handle(HttpContext httpContext) {
        return httpContext.request().body(CreateUserCommand.class)
                .map(commandToUser)
                .map(saveUser)
                .map(UserRepresentation::from)
                .map(Maybe::just)
                .orElse(requestBodyIsNotValid);
    }
}
