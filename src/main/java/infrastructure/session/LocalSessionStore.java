package infrastructure.session;


import annotations.VisibleForTesting;
import domain.model.user.User;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;


/**
 * A session store which is only available on a single node.
 * It will require sticky sessions if the application is deployed in more than one node.
 */
public final class LocalSessionStore implements SessionStore {

    private static final Logger LOGGER = Logger.getLogger(LocalSessionStore.class.getName());

    //TODO this value should be moved to a configuration file
    private static final long FIVE_MINUTES_IN_MILLIS = 5 * 60 * 1000;

    private final ConcurrentHashMap<String, Session> sessionsById = new ConcurrentHashMap<>();
    private final long expirationTimeInMs;

    private static final ScheduledExecutorService expirationService =
            Executors.newSingleThreadScheduledExecutor(new SessionEvictionThreadFactory());

    private final Consumer<Session> expireSession = session -> {
        LOGGER.info("Expiring session " + session.getId());
        deleteSession(session.getId());
    };

    private static class LocalSessionStoreHolder {
        private static final LocalSessionStore INSTANCE = new LocalSessionStore(FIVE_MINUTES_IN_MILLIS);
    }

    public static LocalSessionStore instance() {
        return LocalSessionStoreHolder.INSTANCE;
    }

    @VisibleForTesting
    LocalSessionStore(long expirationTimeInMs){
        this(expirationTimeInMs, true);
    }

    @VisibleForTesting
    LocalSessionStore(long expirationTimeInMs, boolean withAutomaticExpiration){
        this.expirationTimeInMs = expirationTimeInMs;
        if(withAutomaticExpiration) {
            Runnable expirationJob = () -> sessionsById.values().stream()
                                                            .filter(Session::isExpired)
                                                            .forEach(expireSession);
            expirationService.scheduleWithFixedDelay(expirationJob, 1, expirationTimeInMs, TimeUnit.MILLISECONDS);
        }
    }


    @Override
    public Session createSession(User user) {
        return createSession(user, expirationTimeInMs);
    }

    @Override
    public void deleteSession(String sessionId){
        sessionsById.remove(sessionId);
    }

    private Session createSession(User user, long ttlInMillis) {
        Session session = new Session(SessionIdGenerator.generateSessionId(), user, ttlInMillis);
        sessionsById.put(session.getId(), session);
        return session;
    }

    @Override
    public Optional<User> userOf(String sessionId) {
        return Optional.ofNullable(sessionsById.get(sessionId))
                .filter(not(Session::isExpired))
                .map(Session::markAsAccessed)
                .map(Session::getUser);
    }

    private Predicate<? super Session> not(Predicate<? super Session> isExpired) {
        return isExpired.negate();
    }
}
