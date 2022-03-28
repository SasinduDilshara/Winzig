package abstract_machine;

import exceptions.InvalidOperationException;
import exceptions.InvalidUserInputException;

import java.util.ArrayList;
import java.util.Scanner;

public class StackHelper {
    public static Scanner scanner = new Scanner(System.in);

    public static int convertBooleanToInt(boolean x) {
        if (x) {
            return 1;
        }
        return 0;
    }

    public static String generateIntegerNodeName(int n) {
        return "<" + StackConstants.DataMemoryNodeNames.INTEGER + ":" + n + ">";
    }

    public static String generateCharNodeName(char c) {
        return "<" + StackConstants.DataMemoryNodeNames.CHAR + ":" + c + ">";
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
