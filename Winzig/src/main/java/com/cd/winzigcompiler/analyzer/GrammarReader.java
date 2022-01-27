package com.cd.winzigcompiler.analyzer;

import com.cd.winzigcompiler.constants.GrammarConstants;
import com.cd.winzigcompiler.exceptions.WinzigIOException;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static com.cd.winzigcompiler.constants.GrammarConstants.*;

public class GrammarReader {
    private static HashSet<String> nonTerminalSymbols = new HashSet<>();
    private static ArrayList<GrammarRule> grammarRules = new ArrayList<>();
    private static ArrayList<String> nullNonTerminals = new ArrayList<>();
    private static ArrayList<String> allTokens = new ArrayList<>(Arrays.asList(getAllTokens()));
    private static ArrayList<String> terminalTokens = new ArrayList<>(Arrays.asList(terminalArray));
    private static ArrayList<String> nonTerminalTokens = new ArrayList<>(Arrays.asList(nonTerminalArray));

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
                ArrayList<GrammarNode> rightNodes = new ArrayList<>();
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
                if (left.equals("OtherwiseClause")) {
                    //System.out.println(right.length() + " -> " + right + " -> ");
                }
                if (right.length() == 0 || (right.length() == 1 && right.equals( ";"))) {
                    right = nullToken;
                }
                if (right == nullToken) {
                    rightNodes.add(new GrammarNode(nullToken, true, true));
                } else {
                    rightNodes = generateGrammmarNodes(right);
                }
                grammarRules.add(new GrammarRule(left, rightNodes, astHeader));
                astHeader = null;
//                break;
            }
        } catch (IOException e) {
            throw new WinzigIOException("The Format for the grammar rule file is not suitable");
        }
    }

    public static ArrayList<GrammarNode> generateGrammmarNodes(String sequence) {
        String lastChar = "", specialCharacter = "", prevString;
        ArrayList<String> sequenceArray = new ArrayList<>(); String[] tempSequenceArray, tokenSubArray;
        int tokenLength;
        Boolean isArray = false;
        String arraySeparator = null;
        ArrayList<GrammarNode> tokens = new ArrayList<>();
        int i = 0, k = 0;

        sequence = sequence.strip();
        int length = sequence.length();
        if (sequence.endsWith(";")) {
            sequence = sequence.substring(0, sequence.length() - 1);
            length -= 1;
        }
        if (length != 0) {
            lastChar = String.valueOf(sequence.charAt(length - 1));
        }
        tempSequenceArray = sequence.split(" ");

        for (int j = 0; j < tempSequenceArray.length; j++) {
            tempSequenceArray[j] = tempSequenceArray[j].strip();
            if (tempSequenceArray[j] == null) {
                k += 1;
                continue;
            }
            if (j != k) {
                k = j + 1;
                continue;
            }
            if (tempSequenceArray[j].equals("list")) {
                prevString = sequenceArray.remove(j - 1);
//                System.out.println(prevString + " -> " + prevString + " -> " + tempSequenceArray[j + 1] + "[]");
                sequenceArray.add(prevString + " " + tempSequenceArray[j + 1] + " []");
                continue;
            }
            sequenceArray.add(tempSequenceArray[j]);
            k += 1;
//            System.out.println(sequenceArray);
        }

        for (String token: sequenceArray) {
//            System.out.println(token);
            tokenLength = token.length();
            token = token.strip();
            if (token.startsWith("(")) {
                specialCharacter = lastChar;
                token = token.substring(1);
            }
            if (token.endsWith(")+") || token.endsWith(")?")) {
                token = token.substring(0, tokenLength - 2);
            } else if (token.endsWith("*")) {
                token = token.substring(0, tokenLength - 1);
                specialCharacter = lastChar;
            }
            if (token.endsWith("[]")) {
                tokenSubArray = token.split(" ");
                isArray = true;
                token = tokenSubArray[0];
                arraySeparator = tokenSubArray[1];
            } else {
                isArray = false;
                arraySeparator = null;
            }
            if (token.startsWith("'") && token.endsWith("'")) {
                for (String identifier: identifierArray) {
                    if (("'" + identifier + "'").equals(token)) {
                        tokens.add(new GrammarNode(token, true,
                                false, specialCharacter, true, isArray, arraySeparator));
                        break;
                    }
                }
                for (String terminal: terminalTokens) {
                    if (("'" + terminal + "'").equals(token)) {
                        tokens.add(new GrammarNode(token, true, false,
                                specialCharacter, false, isArray, arraySeparator));
                        break;
                    }
                }
            } else {
                for (String nonterminal: nonTerminalTokens) {
                    if (nonterminal.equals(token)) {
                        tokens.add(new GrammarNode(token, true, false,
                                specialCharacter, false, isArray, arraySeparator));
                        break;
                    }
                }
            }
        }
        return tokens;
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
//        int nullNonTerminalSize = nullNonTerminals.size();
//        while (init || nullNonTerminals.size() > nullNonTerminalSize) {
//            init = false;
//            for (GrammarRule gr: grammarRules) {
//                if (gr.getRight() == nullToken) {
//                    nullNonTerminals.add(gr.getLeft());
//                } else if (!nullNonTerminals.contains(gr.getLeft())) {
//                    for (String nullableNonTerminal : nullNonTerminals) {
//                        if (gr.getRight().contains(nullableNonTerminal)) {
//                            if (!temp.contains(gr.getLeft())) {
//                                temp.add(gr.getLeft());
//                            }
//                        }
//                    }
//                }
//            }
//            nullNonTerminals.addAll(temp);
//            nullNonTerminalSize = nullNonTerminals.size();
//        }
    }

    public static void printNullableNonterminals() {
        System.out.println(nullNonTerminals.size() + " nullable non terminals");
        for (String nnt: nullNonTerminals) {
            System.out.println(nnt);
        }
        System.out.println("\n");
    }

    public static String[] getAllTokens() {
        int terminalLength = terminalArray.length;
        int nonTerminalLength = nonTerminalArray.length;

        String[] allTokens = new String[terminalLength + nonTerminalLength];
        System.arraycopy(terminalArray, 0, allTokens, 0, terminalLength);
        System.arraycopy(nonTerminalArray, 0, allTokens, terminalLength, nonTerminalLength);

        return allTokens;
    }
}
