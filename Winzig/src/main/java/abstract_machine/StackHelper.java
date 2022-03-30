package abstract_machine;

import constants.StackConstants;
import exceptions.InvalidUserInputException;

import java.util.Scanner;

public class StackHelper {
    public static Scanner scanner = new Scanner(System.in);

    public static int convertBooleanToInt(boolean x) {
        if (x) {
            return 1;
        }
        return 0;
    }

    public static boolean convertIntToBoolean(Object x) {
        if (((int) x) == 0) {
            return false;
        }
        return true;
    }

    public static String generateIntegerNodeName(int n) {
        return "<" + StackConstants.DataMemoryNodeNames.IntegerNode + ":" + n + ">";
    }

    public static String generateCharNodeName(char c) {
        return "<" + StackConstants.DataMemoryNodeNames.CharNode + ":" + c + ">";
    }

    public static String generateStringNodeName(String s) {
        return "<" + StackConstants.DataMemoryNodeNames.StringNode + ":" + s + ">";
    }

    public static String generateIdentifierNodeName(String id) {
        return "<" + StackConstants.DataMemoryNodeNames.IdentifierNode + ":" + id + ">";
    }

    public static int readUserIntegerInput() throws InvalidUserInputException {
        try {
            return scanner.nextInt();
        } catch (Exception ex) {
            throw new InvalidUserInputException(StackConstants.DataTypes.INT);
        }
    }

    public static char readUserCharacterInput() throws InvalidUserInputException {
            String input = scanner.next();
            if (input.length() > 1) {
                throw new InvalidUserInputException(StackConstants.DataTypes.CHAR);
            }
            return input.charAt(0);
    }
}
