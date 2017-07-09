package domain.model.user;


import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> all();

    void add(User user);

    Optional<User> byId(long id);

    Optional<User> byUsername(String username);

    long nextId();

    void remove(User user);
}
