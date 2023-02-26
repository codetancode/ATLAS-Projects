package customException;
import java.lang.Exception;

public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }

}
