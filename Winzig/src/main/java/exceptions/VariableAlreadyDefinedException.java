package exceptions;

public class VariableAlreadyDefinedException extends Exception {
    public VariableAlreadyDefinedException(String message) {
        super(message);
    }

    public static String generateErrorMessage(String name) {
        //TODO: Handle this for local scope
        return "Varibale: " + name + " Already Defined";
    }
}
