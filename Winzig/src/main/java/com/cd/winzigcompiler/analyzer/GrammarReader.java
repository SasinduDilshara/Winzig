package com.cd.winzigcompiler.analyzer;

import com.cd.winzigcompiler.exceptions.WinzigIOException;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import static com.cd.winzigcompiler.constants.GrammarConstants.*;

public class GrammarReader {
    private static HashSet<String> nonTerminalSymbols = new HashSet<>();
    private static ArrayList<GrammarRule> grammarRules = new ArrayList<>();
    private static ArrayList<String> nullNonTerminals = new ArrayList<>();

    public static void readGrammarRules(String grammarFileName) throws WinzigIOException {
        File file = new File(grammarFileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new WinzigIOException("File not found :- Grammar Rules");
        }

        String st, left, right, astHeader = null, prevLeft = null;
        String[] parts;

        try {
            while (true) {
                if (!((st = br.readLine()) != null)) break;
                if (st.contains(shortArrow)) {
                    parts = st.split(shortArrow);
                    left = parts[0].strip();
                    if (left.length() == 0) {
                        left = prevLeft;
                    } else {
                        if (!nonTerminalSymbols.contains(left) && left.length() !=0 && left != null) {
                            nonTerminalSymbols.add(left);
                        }
                    }
                    right = parts[1].strip();
                } else {
                    throw new WinzigIOException("The Format for the grammar rule file is not suitable(Missing ->)");
                }
                if (st.contains(astArrow)) {
                    parts = right.split(astArrow);
                    right = parts[0].strip();
                    astHeader = parts[1].strip();
                }
                prevLeft = left;
                if (right.length() == 0) {
                    right = nullToken;
                }
                grammarRules.add(new GrammarRule(left, right, astHeader));
                astHeader = null;
            }
        } catch (IOException e) {
            throw new WinzigIOException("The Format for the grammar rule file is not suitable");
        }
    }

    public static void printGrammarRules() {
        for (GrammarRule gr: grammarRules) {
            System.out.println(gr);
        }
        System.out.println("\n");
    }

    public static void writeNonTerminals(String nonTerminalFile) throws WinzigIOException {
        String nonterminals = "";
        for (String nt: nonTerminalSymbols) {
            nonterminals += nt +"\n";
        }
        nonterminals = nonterminals.substring(0, nonterminals.length() - 1);
        try {
            FileWriter myWriter = new FileWriter(nonTerminalFile);
            myWriter.write(nonterminals);
            myWriter.close();
        } catch (Exception e) {
            throw new WinzigIOException("Non Terminal File Doesn't Exists");
        }
    }

    public static void printNonTerminals() {
        System.out.println(nonTerminalSymbols.size() + " non terminals");
        for (String nt: nonTerminalSymbols) {
            System.out.println(nt);
        }
        System.out.println("\n");
    }

    public static void getNullNonTerminals() {
        boolean init = true;
        ArrayList<String> temp = new ArrayList<>();
        int nullNonTerminalSize = nullNonTerminals.size();
        while (init || nullNonTerminals.size() > nullNonTerminalSize) {
            init = false;
            for (GrammarRule gr: grammarRules) {
                if (gr.getRight() == nullToken) {
                    nullNonTerminals.add(gr.getLeft());
                } else if (!nullNonTerminals.contains(gr.getLeft())) {
                    for (String nullableNonTerminal : nullNonTerminals) {
                        if (gr.getRight().contains(nullableNonTerminal)) {
                            if (!temp.contains(gr.getLeft())) {
                                temp.add(gr.getLeft());
                            }
                        }
                    }
                }
            }
            nullNonTerminals.addAll(temp);
            nullNonTerminalSize = nullNonTerminals.size();
        }
    }

    public static void printNullableNonterminals() {
        System.out.println(nullNonTerminals.size() + " nullable non terminals");
        for (String nnt: nullNonTerminals) {
            System.out.println(nnt);
        }
        System.out.println("\n");
    }
}
