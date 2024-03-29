package my.TNTBuilder;

public class TNTException extends Exception {
    public TNTException() {
    }

    public TNTException(String message) {
        super(message);
    }

    public TNTException(String message, Exception cause) {
        super(message, cause);
    }
}
