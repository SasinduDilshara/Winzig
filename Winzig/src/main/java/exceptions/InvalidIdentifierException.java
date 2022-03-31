package exceptions;

public class InvalidIdentifierException extends Exception {

    public InvalidIdentifierException(String message) {
        super(message);
    }

    public static String generateErrorMessage(String name) {
        return "Invalid identifier: " + name;
    }
}
