package scannar;

import java.util.ArrayList;

public class Screener {
    public static ArrayList<Token> screen (ArrayList<Token> tokens) {
        ArrayList<Token> screenTokens = new ArrayList<>();
        for (Token token: tokens) {
            if (token.getLongComment() || token.getShortComment() || token.getSpace()) {
                continue;
            }
            screenTokens.add(token);
        }
        return screenTokens;
    }
}
