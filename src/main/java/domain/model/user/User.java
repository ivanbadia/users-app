package domain.model.user;


import infrastructure.encryption.PasswordEncryptor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class User {
    private long id;
    private String username;
    private String encryptedPassword;
    private Set<Role> roles;


    public User(long id, String username, String password, Set<Role> roles) {
        if(username==null){
            throw new IllegalArgumentException("username is required");
        }

        if(password==null){
            throw new IllegalArgumentException("password is required");
        }

        this.id = id;
        this.username = username;
        this.encryptedPassword = PasswordEncryptor.encrypt(password);
        this.roles = roles==null ? new HashSet<>() : roles;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public boolean isAuthorized(Role role){
        return this.roles.stream().filter(Predicate.isEqual(role)).findFirst().isPresent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(encryptedPassword, user.encryptedPassword) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, encryptedPassword, roles);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", encryptedPassword='").append(encryptedPassword).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }

    public void changePassword(String password) {
        this.encryptedPassword = PasswordEncryptor.encrypt(password);
    }

    public void changeRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean hasPassword(String password) {
        return this.encryptedPassword.equals(PasswordEncryptor.encrypt(password));
    }
}
