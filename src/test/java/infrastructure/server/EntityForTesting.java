package infrastructure.server;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name="entity")
public class EntityForTesting {

    public long id;
    public String message;

    public EntityForTesting() {
    }

    public EntityForTesting(long id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityForTesting that = (EntityForTesting) o;
        return id == that.id &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntityForTesting{");
        sb.append("id=").append(id);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
