package my.TNTBuilder.exception;

public class ValidatorException extends ServiceException{

    public ValidatorException() {
        super();
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Exception cause) {
        super(message, cause);
    }

}
