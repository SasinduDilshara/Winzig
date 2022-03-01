package constants;

import java.util.ArrayList;
import java.util.Arrays;

public class ParserConstants {
    public static final ArrayList<String> primaryBegins =new ArrayList<>(Arrays
            .asList(new String[]{"-", "+", "not", "eof", "(", "succ", "pred", "chr", "ord"}));
    public static final ArrayList<String> expressionSymbols = new ArrayList<>(Arrays
            .asList(new String[]{"<", ">", "<=", ">=", "=", "<>"}));
    public static final ArrayList<String> termSymbols = new ArrayList<>(Arrays
            .asList(new String[]{"+", "-", "or"}));
    public static final ArrayList<String> factorSymbols = new ArrayList<>(Arrays
            .asList(new String[]{"*", "/", "and", "mod"}));
}
