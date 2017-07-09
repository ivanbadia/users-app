package application.user.command;

import domain.model.user.Role;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "user")
public class UpdateUserCommand {

    private String password;
    private Set<Role> roles;

    //Constructor for JAXB
    public UpdateUserCommand() {}

    public UpdateUserCommand(String password,Set<Role> roles) {
        this.password = password;
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }


    @XmlElementWrapper(name = "roles")
    @XmlElement(name = "role")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
