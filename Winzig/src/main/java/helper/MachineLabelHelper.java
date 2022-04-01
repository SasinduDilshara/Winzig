package helper;

public class MachineLabelHelper {
    public static int label = 1;
    public static int generateLabel() {
        return label++;
    }
}
