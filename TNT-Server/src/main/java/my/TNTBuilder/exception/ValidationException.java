package my.TNTBuilder.exception;

public class ValidationException extends ServiceException{

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Exception cause) {
        super(message, cause);
    }

}
