package application.user.representation;

import domain.model.user.Role;
import domain.model.user.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;
import java.util.Set;


@XmlRootElement(name = "user")
@XmlType(propOrder={"id", "username", "roles"})
public class UserRepresentation {
    private long id;
    private String username;
    private Set<Role> roles;

    public UserRepresentation() {
    }

    public UserRepresentation(long id, String username, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public static UserRepresentation from(User user){
        return new UserRepresentation(user.getId(), user.getUsername(), user.getRoles());
    }


    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }


    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRepresentation that = (UserRepresentation) o;
        return id == that.id &&
                Objects.equals(username, that.username) &&
                Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, roles);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserRepresentation{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }
}
