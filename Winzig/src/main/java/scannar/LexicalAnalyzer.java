package scannar;

import exceptions.WinzigScannarException;

import java.util.ArrayList;

import static scannar.Screener.screen;

public class LexicalAnalyzer {
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
