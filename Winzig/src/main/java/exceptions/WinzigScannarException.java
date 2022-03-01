package exceptions;

public class WinzigScannarException extends Exception {

    public WinzigScannarException(String message) {
        super(message);
    }

    public static String getScanError(String key) {
        return "Unexpected " + key + " in the code.";
    }
}