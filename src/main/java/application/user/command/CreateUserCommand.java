package application.user.command;

import domain.model.user.Role;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "user")
public class CreateUserCommand {

    private String username;
    private String password;
    private Set<Role> roles;

    //Constructor for JAXB
    public CreateUserCommand() {}

    public CreateUserCommand(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
