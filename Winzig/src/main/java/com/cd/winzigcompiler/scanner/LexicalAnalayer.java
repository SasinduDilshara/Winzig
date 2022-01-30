package com.cd.winzigcompiler.scanner;

import com.cd.winzigcompiler.exceptions.WinzigScannarException;

import java.util.ArrayList;

import static com.cd.winzigcompiler.scanner.Screener.screen;

public class LexicalAnalayer {
    private ArrayList<Token> tokenArrayList;
    private int currentTokenPosition = 0;

    public ArrayList<Token> getTokenArrayList() {
        return tokenArrayList;
    }

    public Token getNextToken() {
        if (getCurrentTokenPosition() < getTokenArrayList().size()) {
            return getTokenArrayList().get(getAndincrementCurrentTokenPosition());
        } else {
            return null;
        }
    }

    public void setTokenArrayList(ArrayList<Token> tokenArrayList) {
        this.tokenArrayList = tokenArrayList;
    }

    public void ScanAndScreen(String input) throws WinzigScannarException {
        setTokenArrayList(screen(Scanner.scanAndGenerateTokenList(input)));
    }

    public int getCurrentTokenPosition() {
        return currentTokenPosition;
    }

    public void setCurrentTokenPosition(int currentTokenPosition) {
        this.currentTokenPosition = currentTokenPosition;
    }

    public int getAndincrementCurrentTokenPosition() {
        int copyOfCurrentPosition = getCurrentTokenPosition();
        setCurrentTokenPosition(getCurrentTokenPosition() + 1);
        return copyOfCurrentPosition;
    }
}
