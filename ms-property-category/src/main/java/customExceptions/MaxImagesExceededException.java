package customExceptions;

public class MaxImagesExceededException extends RuntimeException {
    public MaxImagesExceededException(String message) {
        super(message);
    }
}
