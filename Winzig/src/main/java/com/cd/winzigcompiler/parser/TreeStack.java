package com.cd.winzigcompiler.parser;

import java.util.ArrayList;

public class TreeStack {

    private ArrayList<TreeNode> treeStack;

    public TreeStack() {
        this.treeStack = new ArrayList<>();
    }

    public ArrayList<TreeNode> getTreeStack() {
        return treeStack;
    }

    public void setTreeStack(ArrayList<TreeNode> treeStack) {
        this.treeStack = treeStack;
    }

    public int size() {
        return getTreeStack().size();
    }

    public void push(TreeNode treeNode) {
        getTreeStack().add(treeNode);
    }

    public TreeNode pop() {
        return getTreeStack().remove(size() - 1);
    }

    @Override
    public String toString() {
        return "TreeStack{" +
                "treeStack=" + treeStack +
                '}';
    }
}
