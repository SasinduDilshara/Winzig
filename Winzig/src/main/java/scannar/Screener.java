package scannar;

import java.util.ArrayList;

public class Screener {
    public static ArrayList<Token> screen (ArrayList<Token> tokens) {
        tokens.removeIf(Token::getSpace);
        tokens.removeIf(Token::getLongComment);
        tokens.removeIf(Token::getShortComment);
        return tokens;
    }
}
