package com.cd.winzigcompiler.analyzer;

public class GrammarNode {
    private String name;
    private Boolean isTerminal;
    private Boolean isNullable;
    private String specialCharacter;

    public GrammarNode(String name, Boolean isTerminal, Boolean isNullable, String specialCharacter) {
        this.name = name;
        this.isTerminal = isTerminal;
        this.isNullable = isNullable;
        this.specialCharacter = specialCharacter;
    }

    public GrammarNode(String name) {
        this.name = name;
        this.isTerminal = false;
        this.isNullable = false;
        this.specialCharacter = null;
    }

    public GrammarNode(String name, Boolean isTerminal) {
        this.name = name;
        this.isTerminal = isTerminal;
        this.isNullable = false;
        this.specialCharacter = null;
    }

    public GrammarNode(String name, Boolean isTerminal, Boolean isNullable) {
        this.name = name;
        this.isTerminal = isTerminal;
        this.isNullable = isNullable;
        this.specialCharacter = null;
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

    public String getSpecialCharacter() {
        return specialCharacter;
    }

    public void setSpecialCharacter(String specialCharacter) {
        this.specialCharacter = specialCharacter;
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
            if (isTerminal) {
                return "'" + name + "' ";
            } else {
                return name + " ";
            }
        }
}
