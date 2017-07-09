package application.user.representation;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="users")
public class UserRepresentationList {
    private List<UserRepresentation> users;

    public UserRepresentationList() {
    }

    @JsonCreator
    public UserRepresentationList(List<UserRepresentation> users) {
        this.users = users;
    }

    @XmlElement(name = "user")
    @JsonValue
    public List<UserRepresentation> getUsers() {
        return users;
    }

    public void setUsers(List<UserRepresentation> users) {
        this.users = users;
    }
}
