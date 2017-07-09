package infrastructure.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name="error")
class Error {

    @JsonIgnore
    private int statusCode;

    private String message;

    //constructor for JAXB
    private Error() {
    }

    public Error(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error error = (Error) o;
        return statusCode == error.statusCode &&
                Objects.equals(message, error.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, message);
    }

    @Override
    public String toString() {
        return message;
    }
}
