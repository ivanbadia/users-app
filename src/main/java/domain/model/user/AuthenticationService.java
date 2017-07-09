package domain.model.user;

import java.util.Optional;

public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> authenticate(String username, String password){

        validateCredentials(username, password);

        return userRepository.byUsername(username)
                .filter(user -> user.hasPassword(password));
    }

    private void validateCredentials(String username, String password) {
        if(username==null) {
            throw new IllegalArgumentException("username is required");
        }

        if(password==null){
            throw new IllegalArgumentException("password is required");
        }
    }
}
