package scanner;

import constants.ScannarConstants;
import exceptions.WinzigScannarException;

import java.util.ArrayList;

public class Scanner {

    public static void printScannarTokenArray (ArrayList<Token> tokens) {
         System.out.println("There are " + tokens.size() + " Number of Tokens");
        for (Token token: tokens) {
             System.out.println(token);
        
        }
         System.out.println("\n");
    }

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
            
                if (ScannarConstants.stableKeywordTokens.contains(currentTokenString)) {
                
                    if (!ScannarConstants.numericAndAlphabeticLetters.contains(nextString)) {
                        generateTokenAndAdd(currentTokenString, ScannarConstants.predefinedToken, tokenArrayList);
                        currentTokenString = "";
                    } else {
                        continue;
                    }
                } else if (ScannarConstants.stableSymbolTokens.contains(currentTokenString)
                        || ScannarConstants.predictableTokens.contains(currentTokenString)) {
                
                    generateTokenAndAdd(currentTokenString, ScannarConstants.predefinedToken, tokenArrayList);
                    currentTokenString = "";
                } else if (ScannarConstants.unpredictableTokens.contains(currentTokenString)) {
                
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
                
                
                    generateTokenAndAdd(currentTokenString, ScannarConstants.spaceToken, tokenArrayList);
                    currentTokenString = "";
                } else if (currentTokenString.equals("\"")) {
                
                    currentTokenString = "";
                    stringState = true;
                    continue;
                } else if (currentTokenString.equals("\'")) {
                
                    currentTokenString = "";
                    charState = true;
                    continue;
                } else if (currentTokenString.equals("{")) {
                
                    currentTokenString = "";
                    longCommentState = true;
                    continue;
                } else if (currentTokenString.equals("#")) {
                
                    currentTokenString = "";
                    shortCommentState = true;
                    continue;
                } else if (ScannarConstants.numericLetters.contains(currentTokenString)) {
                
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
                    if (ScannarConstants.numericAndAlphabeticLetters.contains(nextString)) {
                    
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
            
                if (stringState && current.equals("\"")) {
                
                    stringState = false;
                    generateTokenAndAdd(currentTokenString.substring(0, currentTokenString.length() - 1),
                            ScannarConstants.stringToken, tokenArrayList);
                    currentTokenString = "";
                } else if (charState && current.equals("\'")) {
                
                    charState = false;
                    generateTokenAndAdd(currentTokenString.substring(0, currentTokenString.length() - 1),
                            ScannarConstants.charToken, tokenArrayList);
                    currentTokenString = "";
                } else if (longCommentState && current.equals("}")) {
                
                    longCommentState = false;
                    generateTokenAndAdd(currentTokenString.substring(0, currentTokenString.length() - 1),
                            ScannarConstants.longCommentToken, tokenArrayList);
                    currentTokenString = "";
                } else if (shortCommentState && (current.equals("\n") || current.equals("\n\r"))) {
                
                    shortCommentState = false;
                    generateTokenAndAdd(currentTokenString.substring(0, currentTokenString.length() - 1),
                            ScannarConstants.shortCommentToken, tokenArrayList);
                    currentTokenString = "";
                } else if (intState) {
                
                    if (ScannarConstants.numericLetters.contains(nextString)) {
                        continue;
                    } else {
                        intState = false;
                        generateTokenAndAdd(currentTokenString,
                                ScannarConstants.intToken, tokenArrayList);
                        currentTokenString = "";
                    }
                } else if (identifyState) {
                
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
