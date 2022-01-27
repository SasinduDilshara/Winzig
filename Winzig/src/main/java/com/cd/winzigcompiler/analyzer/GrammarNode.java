package com.cd.winzigcompiler.analyzer;

public class GrammarNode {
    private String name;
    private Boolean isTerminal;
    private Boolean isNullable;
    private Boolean isStarEnable;
    private Boolean isPlusEnable;
    private Boolean isQuestionEnable;

    public GrammarNode(String name, Boolean isTerminal, Boolean isNullable, String specialCharacter) {
        this.name = name;
        this.isTerminal = isTerminal;
        this.isNullable = isNullable;
        if (specialCharacter.equals("*")){
            this.isStarEnable = true;
            this.isPlusEnable = false;
            this.isQuestionEnable = false;
        } else if (specialCharacter.equals("+")){
            this.isStarEnable = false;
            this.isPlusEnable = true;
            this.isQuestionEnable = false;
        } else if (specialCharacter.equals("?")){
            this.isStarEnable = false;
            this.isPlusEnable = false;
            this.isQuestionEnable = true;
        } else {
            this.isStarEnable = false;
            this.isPlusEnable = false;
            this.isQuestionEnable = false;
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
        this.isNullable = false;
        this.isStarEnable = false;
        this.isPlusEnable = false;
        this.isQuestionEnable = false;
    }

    public GrammarNode(String name, Boolean isTerminal, Boolean isNullable) {
        this.name = name;
        this.isTerminal = isTerminal;
        this.isNullable = isNullable;
        this.isStarEnable = false;
        this.isPlusEnable = false;
        this.isQuestionEnable = false;
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
//    @Override
//    public String toString() {
//        return "GrammarNode{" +
//                "name='" + name + '\'' +
//                ", isTerminal=" + isTerminal +
//                ", isNullable=" + isNullable +
//                '}';
//    }

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
            }
            return  string;
        }
}
