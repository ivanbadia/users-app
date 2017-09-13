package infrastructure.persistence.user;


import annotations.VisibleForTesting;
import domain.model.user.DuplicatedUserException;
import domain.model.user.User;
import domain.model.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryUserRepository implements UserRepository{
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final AtomicLong sequenceId = new AtomicLong(1);


    private static class InMemoryUserRepositoryHolder {
        private static final InMemoryUserRepository INSTANCE = new InMemoryUserRepository();
    }

    public static InMemoryUserRepository instance() {
        return InMemoryUserRepositoryHolder.INSTANCE;
    }

    @VisibleForTesting
    InMemoryUserRepository(){}

    @Override
    public Collection<User> all() {
        return Collections.unmodifiableCollection(users.values());
    }

    @Override
    public void add(User user) {
        if (users.containsKey(user.getUsername())) {
            throw new DuplicatedUserException("The user "+user.getUsername()+" already exists");
        }
        users.put(user.getUsername(), user);
    }

    @Override
    public Optional<User> byId(long id) {
        return all().stream().filter(user -> user.getId()==id).findFirst();
    }

    @Override
    public Optional<User> byUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public long nextId() {
        return sequenceId.getAndIncrement();
    }

    @Override
    public void remove(User user) {
        users.remove(user.getUsername());
    }
}
