package com.cd.winzigcompiler.constants;

import java.util.ArrayList;
import java.util.Arrays;

public class ParserConstants {
    public static final ArrayList<String> primaryBegins = (ArrayList<String>) Arrays
            .asList(new String[]{"-", "+", "not", "eof", "(", "succ", "pred", "chr", "ord"});
    public static final ArrayList<String> expressionSymbols = (ArrayList<String>) Arrays
            .asList(new String[]{"<", ">", "<=", ">=", "=", "<>"});
}
