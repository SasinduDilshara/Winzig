package com.cd.winzigcompiler.scanner;

import com.cd.winzigcompiler.analyzer.GrammarRule;
import com.cd.winzigcompiler.constants.ScannarConstants;
import com.cd.winzigcompiler.exceptions.WinzigIOException;
import com.cd.winzigcompiler.exceptions.WinzigScannarException;

import java.util.ArrayList;

public class Scanner {

//    public static void add(ArrayList<Token> tokenArrayList,
//                                   String currentTokenString, String currentTokenState) {
//        tokenArrayList.add(new Token(currentTokenString, currentTokenState));
//    }
//
//    public static void popAndPush(ArrayList<Token> tokenArrayList,
//                                   String currentTokenString, String currentTokenState) {
//        tokenArrayList.remove(tokenArrayList.size() - 1);
//        tokenArrayList.add(new Token(currentTokenString, currentTokenState));
//    }
//
    public static void printScannarTokenArray (ArrayList<Token> tokens) {
         System.out.println("There are " + tokens.size() + " Number of Tokens");
        for (Token token: tokens) {
             System.out.println(token);
            // System.out.println("\n");
        }
         System.out.println("\n");
    }
//
//    public static ArrayList<Token> scan(String input) throws WinzigScannarException {
////        // System.out.println(input);
//        ArrayList<Token> tokenArrayList = new ArrayList<>();
//        String currentTokenString = "", prevTokenString = "", nextTokenString = "", tempCurrentTokenString = "";
//        String currentTokenState = "", prevTokenState = "";
//        String current;
//        Boolean stringState = false, charState = false, longCommentState = false, shortCommentState = false,
//                intState = false, keywordToken = false, finishedPreToken = false, identifierState = false;
////        String next;
//
//        for (int index = 0; index < input.length();  index++) {
//            current = String.valueOf(input.charAt(index));
////            next = String.valueOf(input.charAt(index + 1));
//            currentTokenString += current;
//            if (tempCurrentTokenString.length() != 0) {
//                tempCurrentTokenString += current;
//            }
////            TODO: check how new line works
//            if (keywordToken && !(current.equals(" ") || current.equals("\n"))) {
//                keywordToken = false;
//                currentTokenString = (tokenArrayList.remove(tokenArrayList.size() - 1)).getName() + current;
//            } else if (keywordToken && (current.equals(" ") || current.equals("\n"))){
//                keywordToken = false;
//                currentTokenString = current;
//            }
//            //TODO: Use another state like just finished
//            if (currentTokenState.equals(ScannarConstants.longCommentToken)
//                    && !(current.equals(" ") || current.equals("\n")) && finishedPreToken) {
//                currentTokenString = (tokenArrayList.remove(tokenArrayList.size() - 1)).getName() + current;
//                finishedPreToken = false;
//            } //TODO can we define idetifier and next \n
//            //TODO = For Strings?
//            if ((currentTokenState.equals(ScannarConstants.charToken))
//                    && !(" \n=;,<>=<=:=:+-*/)".contains(current)) && finishedPreToken) {
//                currentTokenString = (tokenArrayList.remove(tokenArrayList.size() - 1)).getName() + current;
//                finishedPreToken = false;
//            }
//            if (intState && " \n=;,<>=<=:=:+-*/)".contains(current)) {
//                currentTokenState = ScannarConstants.intToken;
//                intState = false;
//                add(tokenArrayList, currentTokenString.substring(0, currentTokenString.length() - 1), currentTokenState);
//                currentTokenString = current;
//            }
//            if ((currentTokenState.equals(ScannarConstants.stringToken))
//                    && !(" \n,;)".contains(current)) && finishedPreToken) {
//                currentTokenString = (tokenArrayList.remove(tokenArrayList.size() - 1)).getName() + current;
//                finishedPreToken = false;
//            }
//            if (identifierState && (" :.".contains(current) )) {
////                // System.out.println("cc " + currentTokenString + " " + (currentTokenString.length() - 1) + " " + currentTokenString.substring(0, currentTokenString.length() - 1));
//                currentTokenState = ScannarConstants.identifierToken;
//                identifierState = false;
////                // System.out.println("beforetokenArrayList Length " + tokenArrayList.size());
//                add(tokenArrayList, currentTokenString
//                        .substring(0, currentTokenString.length() - 1), currentTokenState);
////                // System.out.println("aftertokenArrayList Length " + tokenArrayList.size());
//                currentTokenString = current;
//            }
//            if (currentTokenString.equals("\"")) {
//                currentTokenState = ScannarConstants.stringToken;
//                stringState = true;
//                currentTokenString = "";
//            } else if (stringState && current.equals("\"")) {
//                currentTokenState = ScannarConstants.stringToken;
//                stringState = false;
//                add(tokenArrayList, currentTokenString
//                        .substring(0, currentTokenString.length() - 1), currentTokenState);
//                currentTokenString = "";
//                finishedPreToken = true;
//            } else if (currentTokenString.equals("\'")) {
//                currentTokenState = ScannarConstants.charToken;
//                charState = true;
//                currentTokenString = "";
//            } else if (charState && current.equals("\'")) {
//                currentTokenState = ScannarConstants.charToken;
//                if (currentTokenString.length() != 2) {
//                    throw new WinzigScannarException(WinzigScannarException
//                            .getScanError("char -  " + "\'" + currentTokenString));
//                }
//                charState = false;
//                add(tokenArrayList, currentTokenString
//                        .substring(0, currentTokenString.length() - 1), currentTokenState);
//                currentTokenString = "";
//                finishedPreToken = true;
//            } else if (!longCommentState && currentTokenString.equals("{")) {
//                currentTokenState = ScannarConstants.longCommentToken;
//                longCommentState = true;
//                currentTokenString = "";
//            } else if (longCommentState && current.equals("}")) {
//                currentTokenState = ScannarConstants.longCommentToken;
//                longCommentState = false;
//                add(tokenArrayList, currentTokenString
//                        .substring(0, currentTokenString.length() - 1), currentTokenState);
//                currentTokenString = "";
//                finishedPreToken = true;
//            } else if (!shortCommentState && currentTokenString.equals("#")) {
//                currentTokenState = ScannarConstants.shortCommentToken;
//                shortCommentState = true;
//                currentTokenString = "";
//            } else if (shortCommentState && current.equals("\n")) {
//                currentTokenState = ScannarConstants.shortCommentToken;
//                shortCommentState = false;
//                add(tokenArrayList, currentTokenString
//                        .substring(0, currentTokenString.length() - 1), currentTokenState);
//                currentTokenString = "";
//            } else if (stringState || charState || longCommentState || shortCommentState
//                    || (intState && "0123456789".contains(current))) {
//                // No need to create a token. Just Pass
//            } else if (!intState && "0123456789".contains(currentTokenString)) {
//                currentTokenState = ScannarConstants.intToken;
//                intState = true;
//                finishedPreToken = true;
//            } else if ((intState && !("0123456789 ".contains(current)))) {
//                throw new WinzigScannarException(WinzigScannarException
//                        .getScanError("key :- " + currentTokenString));
//            } else {
//                if (current.isBlank() || current.equals("\n")) {
//                    currentTokenState = ScannarConstants.spaceToken;
//                    // System.out.println(current.equals("\n"));
//                    if (!(prevTokenState.equals(ScannarConstants.spaceToken))) {
//                        add(tokenArrayList, currentTokenString, currentTokenState);
//                        currentTokenString = "";
//                    }
//                } else if (ScannarConstants.predictableTokens.contains(tempCurrentTokenString)) {
//                    currentTokenState = ScannarConstants.predefinedToken;
//                    popAndPush(tokenArrayList, currentTokenString, currentTokenState);
//                    currentTokenString = "";
//                    tempCurrentTokenString = "";
//                } else if (ScannarConstants.stableKeywordTokens.contains(currentTokenString)) {
//                    currentTokenState = ScannarConstants.predefinedToken;
//                    add(tokenArrayList, currentTokenString, currentTokenState);
//                    currentTokenString = "";
//                    keywordToken = true;
//                } else if (ScannarConstants.stableSymbolTokens.contains(currentTokenString)) {
//                    currentTokenState = ScannarConstants.predefinedToken;
//                    add(tokenArrayList, currentTokenString, currentTokenState);
//                    currentTokenString = "";
//                } else if (ScannarConstants.unpredictableTokens.contains(currentTokenString)) {
//                    currentTokenState = ScannarConstants.predefinedToken;
//                    add(tokenArrayList, currentTokenString, currentTokenState);
//                    tempCurrentTokenString = currentTokenString;
//                    currentTokenString = "";
//                } else {
//                    if ("abcdefghijklmnopqrstuvwxyz_".contains(currentTokenString)) {
//                        currentTokenState = ScannarConstants.identifierToken;
////                        // System.out.println("current :- " + current + " state :- " + currentTokenState + " string :- " + currentTokenString);
//                        identifierState = true;
//                    }
//                    else if (identifierState && ("1234567890abcdefghijklmnopqrstuvwxyz").contains(current)) {
//                        currentTokenState = ScannarConstants.identifierToken;
//                        identifierState = true;
//                    } else {
//                        throw new WinzigScannarException("input :- " + currentTokenString);
//                    }
//                }
//            }
//            prevTokenString = currentTokenString;
//            prevTokenState = currentTokenState;
//            // System.out.println(current + " " + currentTokenString + " " + currentTokenState + " " + identifierState);
//            printScannarTokenArray(tokenArrayList);
//        }
////TODO COmplete the scannar for finishing value, ex: finishing with an identifier
//        return tokenArrayList;
//    }

    public static void generateTokenAndAdd(String currentTokenString,
           String currentTokenState, ArrayList<Token> tokenArrayList) {
        tokenArrayList.add(new Token(currentTokenString, currentTokenState));
    }

    public static ArrayList<Token> scanAndGenerateTokenList(String input) throws WinzigScannarException {
        ArrayList<Token> tokenArrayList = new ArrayList<>();
        String current;
        Boolean intState = false, charState = false, stringState = false,
                longCommentState = false, shortCommentState = false, ongoingToken = false, identifyState = false;
        String currentTokenString = "", currentTokenState = "", nextString = null;
        String cont = "";
        for (int index = 0; index < input.length();  index++) {
            current = String.valueOf(input.charAt(index));
            if (index != input.length() - 1) {
                nextString = String.valueOf(input.charAt(index + 1));
            } else {
                nextString = null;
            }
            currentTokenString += current;
            cont += current;
            ongoingToken = intState || charState || stringState || longCommentState
                    || shortCommentState || identifyState;
            if (!ongoingToken) {
                // System.out.println("I am in if " + currentTokenString.equals(" \n") + " " + currentTokenString.equals(" "));
                if (ScannarConstants.stableKeywordTokens.contains(currentTokenString)) {
                    // System.out.println("I am 1");
                    if (!ScannarConstants.numericAndAlphabeticLetters.contains(nextString)) {
                        generateTokenAndAdd(currentTokenString, ScannarConstants.predefinedToken, tokenArrayList);
                        currentTokenString = "";
                    } else {
                        continue;
                    }
                } else if (ScannarConstants.stableSymbolTokens.contains(currentTokenString)
                        || ScannarConstants.predictableTokens.contains(currentTokenString)) {
                    // System.out.println("I am 2");
                    generateTokenAndAdd(currentTokenString, ScannarConstants.predefinedToken, tokenArrayList);
                    currentTokenString = "";
                } else if (ScannarConstants.unpredictableTokens.contains(currentTokenString)) {
                    // System.out.println("I am 3");
                    if (currentTokenString.equals(":")) {
                        if (nextString.equals("=")) {
                            continue;
                        } else {
                            generateTokenAndAdd(currentTokenString, ScannarConstants.predefinedToken, tokenArrayList);
                            currentTokenString = "";
                        }
                    } else if (currentTokenString.equals(":=")) {
                        if (nextString.equals(":")) {
                            continue;
                        } else {
                            generateTokenAndAdd(currentTokenString, ScannarConstants.predefinedToken, tokenArrayList);
                            currentTokenString = "";
                        }
                    } else if (currentTokenString.equals(">")) {
                        if (nextString.equals("=")) {
                            continue;
                        } else {
                            generateTokenAndAdd(currentTokenString, ScannarConstants.predefinedToken, tokenArrayList);
                            currentTokenString = "";
                        }
                    } else if (currentTokenString.equals("<")) {
                        if (nextString.equals("=")) {
                            continue;
                        } else if (nextString.equals(">")) {
                            continue;
                        } else {
                            generateTokenAndAdd(currentTokenString, ScannarConstants.predefinedToken, tokenArrayList);
                            currentTokenString = "";
                        }
                    } else if (currentTokenString.equals(".")) {
                        if (nextString.equals(".")) {
                            continue;
                        } else {
                            generateTokenAndAdd(currentTokenString, ScannarConstants.predefinedToken, tokenArrayList);
                            currentTokenString = "";
                        }
                    }
                } else if (ScannarConstants.spaceAndLineBreakerTokens.contains(currentTokenString)) {
                    // System.out.println("I am 4");
                    // System.out.println("SPACE TOKEN IS HERE " + currentTokenString + " . " + nextString);
                    generateTokenAndAdd(currentTokenString, ScannarConstants.spaceToken, tokenArrayList);
                    currentTokenString = "";
                } else if (currentTokenString.equals("\"")) {
                    // System.out.println("I am 5");
                    currentTokenString = "";
                    stringState = true;
                    continue;
                } else if (currentTokenString.equals("\'")) {
                    // System.out.println("I am 6");
                    currentTokenString = "";
                    charState = true;
                    continue;
                } else if (currentTokenString.equals("{")) {
                    // System.out.println("I am 7");
                    currentTokenString = "";
                    longCommentState = true;
                    continue;
                } else if (currentTokenString.equals("#")) {
                    // System.out.println("I am 8");
                    currentTokenString = "";
                    shortCommentState = true;
                    continue;
                } else if (ScannarConstants.numericLetters.contains(currentTokenString)) {
                    // System.out.println("I am 9");
                    if (ScannarConstants.numericLetters.contains(nextString)) {
                        intState = true;
                        continue;
                    } else {
                        intState = false;
                        generateTokenAndAdd(currentTokenString,
                                ScannarConstants.intToken, tokenArrayList);
                        currentTokenString = "";
                    }
                } else if (ScannarConstants.alphaticLetters.contains(currentTokenString)
                        || current.equals("_")) {
                    if (ScannarConstants.alphaticLetters.contains(nextString)) {
                        // System.out.println("I am 10");
                        identifyState = true;
                        continue;
                    } else {
                        identifyState = false;
                        generateTokenAndAdd(currentTokenString,
                                ScannarConstants.identifierToken, tokenArrayList);
                        currentTokenString = "";
                    }
                }
            } else {
                // System.out.println("I am in else");
                if (stringState && current.equals("\"")) {
                    // System.out.println("I am 11");
                    stringState = false;
                    generateTokenAndAdd(currentTokenString.substring(0, currentTokenString.length() - 1),
                            ScannarConstants.stringToken, tokenArrayList);
                    currentTokenString = "";
                } else if (charState && current.equals("\'")) {
                    // System.out.println("I am 12");
                    charState = false;
                    generateTokenAndAdd(currentTokenString.substring(0, currentTokenString.length() - 1),
                            ScannarConstants.charToken, tokenArrayList);
                    currentTokenString = "";
                } else if (longCommentState && current.equals("}")) {
                    // System.out.println("I am 13");
                    longCommentState = false;
                    generateTokenAndAdd(currentTokenString.substring(0, currentTokenString.length() - 1),
                            ScannarConstants.longCommentToken, tokenArrayList);
                    currentTokenString = "";
                } else if (shortCommentState && (current.equals("\n") || current.equals("\n\r"))) {
                    // System.out.println("I am 14");
                    shortCommentState = false;
                    generateTokenAndAdd(currentTokenString.substring(0, currentTokenString.length() - 1),
                            ScannarConstants.shortCommentToken, tokenArrayList);
                    currentTokenString = "";
                } else if (intState) {
                    // System.out.println("I am 15");
                    if (ScannarConstants.numericLetters.contains(nextString)) {
                        continue;
                    } else {
                        intState = false;
                        generateTokenAndAdd(currentTokenString,
                                ScannarConstants.intToken, tokenArrayList);
                        currentTokenString = "";
                    }
                } else if (identifyState) {
                    // System.out.println("I am 16");
                    if (ScannarConstants.numericAndAlphabeticLetters.contains(nextString)) {
                        continue;
                    } else {
                        if (ScannarConstants.stableKeywordTokens.contains(currentTokenString)) {
                            currentTokenState = ScannarConstants.predefinedToken;
                        } else {
                            currentTokenState = ScannarConstants.identifierToken;
                        }
                        identifyState = false;
                        generateTokenAndAdd(currentTokenString,
                                currentTokenState, tokenArrayList);
                        currentTokenString = "";
                    }
                }
            }

//            // System.out.println("'" + current + "'a :- b'" + currentTokenString + "'c d'" + ongoingToken + "' int,string,identify,longcomment,shortcomment,space " + intState.toString() + " " + stringState.toString() + " " + identifyState.toString() + " " + longCommentState.toString() + " " + shortCommentState.toString());
//            // System.out.println(ScannarConstants.spaceAndLineBreakerTokens.contains(currentTokenString));
//            // System.out.println(currentTokenState + " 111111111 " + longCommentState + " " + ongoingToken);
//            System.out.println("b" + currentTokenString + "a");
        }
        /*
        * Check the remaining String
        * */
        ongoingToken = intState || charState || stringState || longCommentState
                || shortCommentState || identifyState;
        if (ongoingToken) {
            System.out.println(stringState + " " + charState + " " + longCommentState + " " +shortCommentState + " " + identifyState);
            if (stringState) {
                throw new WinzigScannarException("Unclosed String literal\n :- " + currentTokenString);
            } else if (charState) {
                throw new WinzigScannarException("Unclosed char literal\n :- " + currentTokenString);
            } else if (longCommentState) {
                throw new WinzigScannarException("Unclosed Line Comment literal\n :- " + currentTokenString);
            } else if (shortCommentState) {
                generateTokenAndAdd(currentTokenString,
                        ScannarConstants.shortCommentToken, tokenArrayList);
            } else {
                throw new WinzigScannarException("key :- " + currentTokenString);
            }
        }
        return tokenArrayList;
    }
}
