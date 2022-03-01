package exceptions;

public class WinzigParserException extends Exception{
    public WinzigParserException(String message) {
        super(message);
    }

    public static String generateErrorMessage (String input) {
        return "Invalid input :- " + input;
    }
}
