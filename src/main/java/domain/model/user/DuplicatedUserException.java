package domain.model.user;


public class DuplicatedUserException extends IllegalStateException{
    public DuplicatedUserException(String message) {
        super(message);
    }
}
