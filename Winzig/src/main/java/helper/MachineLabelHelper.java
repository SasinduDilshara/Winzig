package helper;

public class MachineLabelHelper {
    public static int label = 1;
    public static int instructionIndex = 1;
    public static int generateLabel() {
        return label++;
    }
    public static int generateInstructureIndex() { return instructionIndex++; };
}
