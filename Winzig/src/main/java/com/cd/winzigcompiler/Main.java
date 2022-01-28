package com.cd.winzigcompiler;

import com.cd.winzigcompiler.analyzer.GrammarReader;
import com.cd.winzigcompiler.exceptions.WinzigIOException;
import com.cd.winzigcompiler.scanner.Scanner;

public class Main {
    public static void main(String[] args) throws WinzigIOException {
        String currentDirectory = System.getProperty("user.dir");
        String grammarFilePath = "/src/main/java/com/cd/winzigcompiler/grammar/WinZigC_Grammar.txt";
        String nonTerminalsFile = "/src/main/java/com/cd/winzigcompiler/grammar/non_terminal_list.txt";

//        System.out.println(currentDirectory + grammarFilePath);
//        System.out.println(currentDirectory + nonTerminalsFile);

        // GRAMMAR RELATED WORK

        GrammarReader.readGrammarRules(currentDirectory + grammarFilePath);
        GrammarReader.getNullNonTerminals();

//        GrammarReader.printGrammarRules();
//        GrammarReader.printNonTerminals();
//        GrammarReader.printNullableNonterminals();

//        GrammarReader.writeNonTerminals(currentDirectory + nonTerminalsFile);

        // SCANNER RELATED WORK

        Scanner.scan("a");

    }
}
