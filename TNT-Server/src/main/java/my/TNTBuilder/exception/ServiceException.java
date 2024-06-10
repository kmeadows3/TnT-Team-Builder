package my.TNTBuilder.exception;

public class ServiceException extends Exception {
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
