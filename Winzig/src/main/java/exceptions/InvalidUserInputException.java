package exceptions;

public class InvalidUserInputException extends Exception{
    public InvalidUserInputException(String expectedType) {
        super(generateErrorMessage(expectedType));
    }
    public static String generateErrorMessage(String expectedType) {
        return "Expected Type " + expectedType + " as User Input!!";
    }
}
