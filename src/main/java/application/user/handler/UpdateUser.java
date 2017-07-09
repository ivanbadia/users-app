package application.user.handler;

import application.user.command.UpdateUserCommand;
import domain.model.user.User;
import domain.model.user.UserRepository;
import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import infrastructure.server.exceptions.BadRequestException;
import infrastructure.server.exceptions.NotFoundException;
import io.reactivex.Maybe;

import java.util.Optional;
import java.util.function.Function;


public class UpdateUser implements Handler{

    private UserRepository userRepository;

    private final Maybe<Object> userNotFound = Maybe.error(new NotFoundException("User not found"));
    private final Maybe<Object> requestBodyIsNotValid = Maybe.error(new BadRequestException("The request body is not valid"));

    private final Function<String, Optional<User>> findUserById = id -> userRepository.byId(Long.valueOf(id));

    public UpdateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Maybe<Object> handle(HttpContext httpContext) {
        return httpContext.request().body(UpdateUserCommand.class)
                .map(executeCommand(httpContext))
                .orElse(requestBodyIsNotValid);
    }

    private Function<UpdateUserCommand, Maybe<Object>> executeCommand(HttpContext httpContext) {
        return updateUserCommand -> httpContext.request().pathParameter("id")
                .flatMap(findUserById)
                .map(updateUser(updateUserCommand))
                .map(user -> Maybe.empty())
                .orElse(userNotFound);
    }

    private Function<User, User> updateUser(UpdateUserCommand updateUserCommand) {
        return user -> {
            user.changePassword(updateUserCommand.getPassword());
            user.changeRoles(updateUserCommand.getRoles());
            return user;
        };
    }
}
