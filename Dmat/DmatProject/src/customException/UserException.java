package customException;
import java.lang.Exception;

public class UserException extends Exception {
    public UserException(String message) {
        super(message);
    }
}

