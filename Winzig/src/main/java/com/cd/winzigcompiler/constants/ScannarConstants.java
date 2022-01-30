package com.cd.winzigcompiler.constants;

import java.util.ArrayList;
import java.util.Arrays;

public class ScannarConstants {
    public static final String predefinedToken = "Predefined";
    public static final String intToken = "int";
    public static final String identifierToken = "identifier";
    public static final String spaceToken = "space";
    public static final String charToken = "char";
    public static final String stringToken = "string";
    public static final String shortCommentToken = "short-comment";
    public static final String longCommentToken = "long-comment";
    public static final ArrayList<String> stableKeywordTokens = new ArrayList<> (Arrays.asList(new String[]{
            "program",
            "var",
            "const",
            "type",
            "function",
            "return",
            "begin",
            "end",
            "output",
            "if",
            "then",
            "else",
            "while",
            "do",
            "case",
            "of",
            "otherwise",
            "repeat",
            "for",
            "until",
            "loop",
            "pool",
            "exit",
            "mod",
            "and",
            "or",
            "not",
            "read",
            "succ",
            "pred",
            "chr",
            "ord",
            "eof"

    }));
    public static final ArrayList<String> stableSymbolTokens = new ArrayList<> (Arrays.asList(new String[]{
            "\\n",
//            ":=:",
//            ":=",
//            "..",
//            "<=",
//            "<>",
//            "<",
//            ">=",
//            ">",
            "=",
//            "{",
//            ":",
            ";",
//            ".",
            ",",
            "(",
            ")",
            "+",
            "-",
            "*",
            "/",

    }));
    public static final ArrayList<String> unpredictableTokens = new ArrayList<> (Arrays.asList(new String[]{
            ":=",
            "<",
            ">",
            ":",
            "."
    }));
    public static final ArrayList<String> predictableTokens = new ArrayList<> (Arrays.asList(new String[]{
            ":=:",
//            ":=",
            "<=",
            "<>",
            ">=",
            "..",
    }));
    public static final String alphaticLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String numericLetters = "0123456789";
    public static final String numericAndAlphabeticLetters = alphaticLetters + numericLetters;
    public static final String spaceAndLineBreakerTokens = " \n \r \r\n \t";
}
