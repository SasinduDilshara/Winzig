package com.cd.winzigcompiler.scanner;

import java.util.ArrayList;

public class Screener {
    public static ArrayList<Token> screen (ArrayList<Token> tokens) {
        //TODO This works fine for Java 16
        tokens.removeIf(Token::getSpace);
        tokens.removeIf(Token::getLongComment);
        tokens.removeIf(Token::getShortComment);
        return tokens;
    }
}
