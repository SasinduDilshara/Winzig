package com.cd.winzigcompiler.constants;

import java.util.ArrayList;
import java.util.Locale;

public class GrammarConstants {
    public static final String shortArrow = "->";
    public static final String astArrow = "=>";
    public static final String nullToken = "NONE";
    public static final String startSymbol = "Winzig";
    public static final String[] identifierArray = {
            "<char>",
            "<string>",
            "<integer>",
            "<identifier>"
    };
    public static final String[] nonTerminalArray = {
            "ConstValue",
            "Types",
            "Dcln",
            "SubProgs",
            "Caseclause",
            "CaseExpression",
            "Name",
            "Assignment",
            "ForStat",
            "Factor",
            "Primary",
            "StringNode",
            "Body",
            "Dclns",
            "OtherwiseClause",
            "Statement",
            "Const",
            "Params",
            "Term",
            "Winzig",
            "Fcn",
            "ForExp",
            "Caseclauses",
            "Type",
            "Consts",
            "Expression",
            "LitList",
            "OutExp"
    };
    public static final String[] terminalArray = {
            "\\n",
            "program",
            "var",
            "const",
            "type",
            "function",
            "return",
            "begin",
            "end",
            ":=:",
            ":=",
            "output",
            "if",
            "then",
            "else",
            "while",
            "do",
            "case",
            "of",
            "..",
            "otherwise",
            "repeat",
            "for",
            "until",
            "loop",
            "pool",
            "exit",
            "<=",
            "<>",
            "<",
            ">=",
            ">",
            "=",
            "mod",
            "and",
            "or",
            "not",
            "read",
            "succ",
            "pred",
            "chr",
            "ord",
            "eof",
            "{",
            ":",
            ";",
            ".",
            ",",
            "(",
            ")",
            "+",
            "-",
            "*",
            "/"
    };
}
