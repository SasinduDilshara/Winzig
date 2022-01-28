package com.cd.winzigcompiler.scanner;

import com.cd.winzigcompiler.constants.ScannarConstants;
import com.cd.winzigcompiler.exceptions.WinzigIOException;
import com.cd.winzigcompiler.exceptions.WinzigScannarException;

import java.util.ArrayList;

public class Scanner {

    public static void add(ArrayList<Token> tokenArrayList,
                                   String currentTokenString, String currentTokenState) {
        tokenArrayList.add(new Token(currentTokenString, currentTokenState));
    }

    public static void popAndPush(ArrayList<Token> tokenArrayList,
                                   String currentTokenString, String currentTokenState) {
        tokenArrayList.remove(tokenArrayList.size() - 1);
        tokenArrayList.add(new Token(currentTokenString, currentTokenState));
    }

    public static ArrayList<Token> scan(String input) throws WinzigScannarException {
        ArrayList<Token> tokenArrayList = new ArrayList<>();
        String currentTokenString = "", prevTokenString = "", nextTokenString = "", tempCurrentTokenString = "";
        String currentTokenState = null, prevTokenState = null;
        String current;
        Boolean stringState = false, charState = false, longCommentState = false, shortCommentState = false, intState = false;
//        String next;

        for (int index = 0; index < input.length();  index++){
            current = String.valueOf(input.charAt(index));
//            next = String.valueOf(input.charAt(index + 1));
            currentTokenString += current;
            if (tempCurrentTokenString.length() != 0) {
                tempCurrentTokenString += current;
            }
            if (currentTokenString.equals("\"")) {
                currentTokenState = ScannarConstants.stringToken;
                if (!stringState) {
                    stringState = true;
                } else {
                    stringState = false;
                    add(tokenArrayList, currentTokenString
                            .substring(0, currentTokenString.length() - 1), currentTokenState);
                }
                currentTokenString = "";
            } else if (currentTokenString.equals("\'")) {
                currentTokenState = ScannarConstants.charToken;
                if (!charState) {
                    charState = true;
                } else {
                    if (currentTokenString.length() != 2) {
                        throw new WinzigScannarException(WinzigScannarException
                                .getScanError("char -  " + "\'" + currentTokenString));
                    }
                    charState = false;
                    add(tokenArrayList, currentTokenString
                            .substring(0, currentTokenString.length() - 1), currentTokenState);
                }
                currentTokenString = "";
            } else if (currentTokenString.equals("{")) {
                currentTokenState = ScannarConstants.longCommentToken;
                longCommentState = true;
                currentTokenString = "";
            } else if (currentTokenString.equals("}")) {
                currentTokenState = ScannarConstants.longCommentToken;
                longCommentState = false;
                add(tokenArrayList, currentTokenString
                        .substring(0, currentTokenString.length() - 1), currentTokenState);
                currentTokenString = "";
            } else if (currentTokenString.equals("#")) {
                currentTokenState = ScannarConstants.shortCommentToken;
                shortCommentState = true;
                currentTokenString = "";
            } else if (shortCommentState && currentTokenString.equals("\\n")) {
                currentTokenState = ScannarConstants.shortCommentToken;
                shortCommentState = false;
                add(tokenArrayList, currentTokenString
                        .substring(0, currentTokenString.length() - 1), currentTokenState);
                currentTokenString = "";
            } else {
                if (current.isBlank()) {
                    currentTokenState = ScannarConstants.spaceToken;
                    if (!(prevTokenState.equals(ScannarConstants.spaceToken))) {
                        add(tokenArrayList, currentTokenString, currentTokenState);
                        currentTokenString = "";
                    }
                } else if (ScannarConstants.predictableTokens.contains(tempCurrentTokenString)) {
                    currentTokenState = ScannarConstants.predefinedToken;
                    popAndPush(tokenArrayList, currentTokenString, currentTokenState);
                    currentTokenString = "";
                    tempCurrentTokenString = "";
                } else if (ScannarConstants.stableTokens.contains(currentTokenString)) {
                    currentTokenState = ScannarConstants.predefinedToken;
                    add(tokenArrayList, currentTokenString, currentTokenState);
                    currentTokenString = "";
                } else if (ScannarConstants.unpredictableTokens.contains(currentTokenString)) {
                    currentTokenState = ScannarConstants.predefinedToken;
                    add(tokenArrayList, currentTokenString, currentTokenState);
                    tempCurrentTokenString = currentTokenString;
                    currentTokenString = "";
                }
            }
            prevTokenString = currentTokenString;
            prevTokenState = currentTokenState;
        }

        return tokenArrayList;
    }
}
