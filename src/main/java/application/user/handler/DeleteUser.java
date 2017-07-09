package application.user.handler;

import domain.model.user.User;
import domain.model.user.UserRepository;
import infrastructure.server.Handler;
import infrastructure.server.HttpContext;
import infrastructure.server.exceptions.NotFoundException;
import io.reactivex.Maybe;

import java.util.Optional;
import java.util.function.Function;


public class DeleteUser implements Handler{

    private UserRepository userRepository;

    private final Maybe userNotFound = Maybe.error(new NotFoundException("User not found"));

    private final Function<String, Optional<User>> findUserById = id -> userRepository.byId(Long.valueOf(id));
    private final Function<User, Maybe> deleteUser = user -> {
        userRepository.remove(user);
        return Maybe.empty();
    };


    public DeleteUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Maybe handle(HttpContext httpContext) {
        return httpContext.request().pathParameter("id")
                    .flatMap(findUserById)
                    .map(deleteUser)
                    .orElse(userNotFound);
    }

}
