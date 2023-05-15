package fa.training.movietheater_mockproject.exception;

public class NotAcceptable extends RuntimeException{
    public NotAcceptable() {
    }

    public NotAcceptable(String message) {
        super(message);
    }
}
