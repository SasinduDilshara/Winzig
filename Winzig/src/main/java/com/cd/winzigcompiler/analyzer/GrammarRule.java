package com.cd.winzigcompiler.analyzer;

public class GrammarRule {
    private String left;
    private String right;
    private String astHeader = null;

    public GrammarRule(String left, String right, String astHeader) {
        this.left = left;
        this.right = right;
        if (astHeader != null) {
            this.astHeader = astHeader;
        }
    }

    public GrammarRule(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getAstHeader() {
        return astHeader;
    }

    public void setAstHeader(String astHeader) {
        this.astHeader = astHeader;
    }

    @Override
    public String toString() {
        if (astHeader == null) {
            return left + " -> " + right;
        }
        return left + " -> " + right + " => " + astHeader;
    }
}
