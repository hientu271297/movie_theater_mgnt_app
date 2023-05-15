package fa.training.movietheater_mockproject.exception;

public class AccessDenied extends RuntimeException{
    public AccessDenied() {
    }

    public AccessDenied(String message) {
        super(message);
    }
}
