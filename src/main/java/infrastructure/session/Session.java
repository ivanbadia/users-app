package infrastructure.session;


import domain.model.user.User;

import java.util.Objects;

public class Session {

    private final String id;
    private final User user;
    private final long ttl;
    private long lastAccess;

    public Session(String id, User user, long ttlInMillis) {
        this.id = id;
        this.ttl = ttlInMillis;
        this.lastAccess = System.currentTimeMillis();
        this.user = user;
    }

    public boolean isExpired(){
        return System.currentTimeMillis()> getExpiration();
    }

    public Session markAsAccessed() {
        this.lastAccess = System.currentTimeMillis();
        return this;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return ttl == session.ttl &&
                lastAccess == session.lastAccess &&
                Objects.equals(id, session.id) &&
                Objects.equals(user, session.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, ttl, lastAccess);
    }

    public long getExpiration() {
        return lastAccess + ttl;
    }
}
