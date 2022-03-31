package exceptions;

public class InvalidValueForIdentifierException extends  Exception {
    public InvalidValueForIdentifierException(String message) {
        super(message);
    }

    public static String generateErrorMessage(String varName, String value) {
        return "Invalid Value: "+ value + " for variable: " + varName;
    }
}
