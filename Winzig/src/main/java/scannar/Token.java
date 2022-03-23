package scannar;

import constants.ScannarConstants;

public class Token {
    private String name;
    private String tokenType;
    private Boolean isIdentifier = false;
    private Boolean isPreIdentifier = false;
    private Boolean isInteger = false;
    private Boolean isSpace= false;
    private Boolean isChar = false;
    private Boolean isString = false;
    private Boolean isShortComment = false;
    private Boolean isLongComment = false;

    public Token(String name, String tokenType) {
        this.name = name;
        this.tokenType = tokenType;
        switch (tokenType) {
            case ScannarConstants.identifierToken:
                this.isIdentifier = true;
                break;
            case ScannarConstants.predefinedToken:
                this.isPreIdentifier = true;
                break;
            case ScannarConstants.intToken:
                this.isInteger = true;
                break;
            case ScannarConstants.spaceToken:
                this.isSpace = true;
                break;
            case ScannarConstants.charToken:
                this.isChar = true;
                break;
            case ScannarConstants.stringToken:
                this.isString = true;
                break;
            case ScannarConstants.shortCommentToken:
                this.isShortComment = true;
                break;
            case ScannarConstants.longCommentToken:
                this.isLongComment = true;
                break;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Boolean getIdentifier() {
        return isIdentifier;
    }

    public void setIdentifier(Boolean identifier) {
        isIdentifier = identifier;
    }

    public Boolean getPreIdentifier() {
        return isPreIdentifier;
    }

    public void setPreIdentifier(Boolean preIdentifier) {
        isPreIdentifier = preIdentifier;
    }

    public Boolean getInteger() {
        return isInteger;
    }

    public void setInteger(Boolean integer) {
        isInteger = integer;
    }

    public Boolean getSpace() {
        return isSpace;
    }

    public void setSpace(Boolean space) {
        isSpace = space;
    }

    public Boolean getChar() {
        return isChar;
    }

    public void setChar(Boolean aChar) {
        isChar = aChar;
    }

    public Boolean getString() {
        return isString;
    }

    public void setString(Boolean string) {
        isString = string;
    }

    public Boolean getShortComment() {
        return isShortComment;
    }

    public void setShortComment(Boolean shortComment) {
        isShortComment = shortComment;
    }

    public Boolean getLongComment() {
        return isLongComment;
    }

    public void setLongComment(Boolean longComment) {
        isLongComment = longComment;
    }

    @Override
    public String toString() {
        String newName = name;
        if (name.equals("\n")) {
            newName = "\\n";
        } else if (name.equals("\t")) {
            newName = "\\t";
        } else if (name.equals("\n\r")) {
            newName = "\\n";
        }
        return "Token{" +
                "name='" + newName + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
