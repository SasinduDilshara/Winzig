package exceptions;

public class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super(generateErrorMessage(message));
    }
    public static String generateErrorMessage(String operation) {
        return "Operator " + operation + " is Invalid!!";
    }
}
