package domain.model;

import domain.model.user.AuthenticationService;
import domain.model.user.UserRepository;
import infrastructure.persistence.user.InMemoryUserRepository;

public class DomainContext {


    public static UserRepository userRepository(){
        return InMemoryUserRepository.instance();
    }

    public static AuthenticationService authenticationService(){
        return new AuthenticationService(userRepository());
    }


}
