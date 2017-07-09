package domain.model.user;

import java.util.HashSet;
import java.util.Set;


public class UserBuilder {

    private long id =1;
    private String username = "username";
    private String password = "password";
    private Set<Role> roles = new HashSet<>();


    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static User anUser() {
        return builder().build();
    }

    public UserBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }


    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User build() {
        return new User(id, username, password, roles);
    }
}
