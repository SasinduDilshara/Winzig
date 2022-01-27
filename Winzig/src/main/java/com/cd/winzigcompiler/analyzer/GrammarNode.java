package com.cd.winzigcompiler.analyzer;

public class GrammarNode {
    private String name;
    private Boolean isTerminal = false;
    private Boolean isNullable = false;
    private Boolean isStarEnable = false;
    private Boolean isPlusEnable = false;
    private Boolean isQuestionEnable = false;
    private Boolean isIdentifier = false;
    private Boolean isArray = false;
    private String arraySeparator = "";

    public GrammarNode(String name, Boolean isTerminal, Boolean isNullable, String specialCharacter) {
        this.name = name;
        this.isTerminal = isTerminal;
        this.isNullable = isNullable;
        if (specialCharacter.equals("*")){
            this.isStarEnable = true;
        } else if (specialCharacter.equals("+")){
            this.isPlusEnable = true;
        } else if (specialCharacter.equals("?")){
            this.isQuestionEnable = true;
        }
    }

    public GrammarNode(String name, Boolean isTerminal, Boolean isNullable, String specialCharacter, Boolean isIdentifier) {
        this.name = name;
        this.isTerminal = isTerminal;
        this.isNullable = isNullable;
        this.isIdentifier = isIdentifier;
        if (specialCharacter.equals("*")){
            this.isStarEnable = true;
        } else if (specialCharacter.equals("+")){
            this.isPlusEnable = true;
        } else if (specialCharacter.equals("?")){
            this.isQuestionEnable = true;
        }
    }

    public GrammarNode(String name, Boolean isTerminal, Boolean isNullable, String specialCharacter, Boolean isIdentifier, Boolean isArray, String arraySeparator) {
        this.name = name;
        this.isTerminal = isTerminal;
        this.isNullable = isNullable;
        this.isIdentifier = isIdentifier;
        this.isArray = isArray;
        this.arraySeparator = arraySeparator;
        if (specialCharacter.equals("*")){
            this.isStarEnable = true;
        } else if (specialCharacter.equals("+")){
            this.isPlusEnable = true;
        } else if (specialCharacter.equals("?")){
            this.isQuestionEnable = true;
        }
    }

    public GrammarNode(String name) {
        this.name = name;
        this.isTerminal = false;
        this.isNullable = false;
        this.isStarEnable = false;
        this.isPlusEnable = false;
        this.isQuestionEnable = false;
    }

    public GrammarNode(String name, Boolean isTerminal) {
        this.name = name;
        this.isTerminal = isTerminal;
    }

    public GrammarNode(String name, Boolean isTerminal, Boolean isNullable) {
        this.name = name;
        this.isTerminal = isTerminal;
        this.isNullable = isNullable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTerminal() {
        return isTerminal;
    }

    public void setTerminal(Boolean terminal) {
        isTerminal = terminal;
    }

    public Boolean getNullable() {
        return isNullable;
    }

    public void setNullable(Boolean nullable) {
        isNullable = nullable;
    }

    public Boolean getStarEnable() {
        return isStarEnable;
    }

    public void setStarEnable(Boolean starEnable) {
        isStarEnable = starEnable;
    }

    public Boolean getPlusEnable() {
        return isPlusEnable;
    }

    public void setPlusEnable(Boolean plusEnable) {
        isPlusEnable = plusEnable;
    }

    public Boolean getQuestionEnable() {
        return isQuestionEnable;
    }

    public void setQuestionEnable(Boolean questionEnable) {
        isQuestionEnable = questionEnable;
    }
    public Boolean getIdentifier() {
        return isIdentifier;
    }

    public void setIdentifier(Boolean identifier) {
        isIdentifier = identifier;
    }

    public Boolean getArray() {
        return isArray;
    }

    public void setArray(Boolean array) {
        isArray = array;
    }

    public String getArraySeparator() {
        return arraySeparator;
    }

    public void setArraySeparator(String arraySeparator) {
        this.arraySeparator = arraySeparator;
    }

    @Override
        public String toString() {
            String string = "";
            if (isTerminal) {
                string += "'" + name + "' ";
            } else {
                string += name + " ";
            }
            if (getPlusEnable()) {
                string += " +";
            } else if (getQuestionEnable()) {
                string += " ?";
            } else if (getStarEnable()) {
                string += " *";
            } else if (getIdentifier()) {
                string += " : <ID>";
            } else if (getArray()) {
                string += " [" + arraySeparator + "]";
            }
            return  string;
        }
}
