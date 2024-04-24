package hexlet.code.exception;

public class ParentEntityExistsException extends RuntimeException {
    public ParentEntityExistsException(String message) {
        super(message);
    }
}
