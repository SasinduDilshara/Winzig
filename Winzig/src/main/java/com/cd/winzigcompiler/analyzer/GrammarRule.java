package com.cd.winzigcompiler.analyzer;

import java.util.ArrayList;

public class GrammarRule {
    private GrammarNode left;
    private ArrayList<GrammarNode> right;
    private String astHeader = null;

    public GrammarRule(GrammarNode left, ArrayList<GrammarNode> right, String astHeader) {
        this.left = left;
        this.right = right;
        if (astHeader != null) {
            this.astHeader = astHeader;
        }
    }

    public GrammarRule(GrammarNode left, ArrayList<GrammarNode> right) {
        this.left = left;
        this.right = right;
    }

    public int getRightLength() {
        return right.size();
    }

    public GrammarNode getLeft() {
        return left;
    }

    public void setLeft(GrammarNode left) {
        this.left = left;
    }

    public ArrayList<GrammarNode> getRight() {
        return right;
    }

    public void setRight(ArrayList<GrammarNode> right) {
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
