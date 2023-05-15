package fa.training.movietheater_mockproject.exception;

public class ResourceNotFound extends RuntimeException{
    public ResourceNotFound() {
    }

    public ResourceNotFound(String message) {
        super(message);
    }
}
