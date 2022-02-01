package com.cd.winzigcompiler.parser;

import java.util.ArrayList;

public class TreeNode {
    private String name;
    private ArrayList<TreeNode> children;
    private int depth;

    public TreeNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.depth = 0;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
        for (TreeNode treeNode : getChildren()) {
            treeNode.setDepth(depth + 1);
        }
    }

    public void addChild(TreeNode treeNode) {
        treeNode.setDepth(this.depth + 1);
        getChildren().add(treeNode);
    }

    public String traverseTree() {
        return traverseTree(0);
    }

    public String traverseTree(int depth) {
//        TreeNode treeNode;
        String traverseResults = "";
        for (int i = 0; i < depth; i++) {
            traverseResults += ".";
        }
        traverseResults += getName() + "(" + getChildren().size() + ")" + "\n";
//        System.out.println("\n");
        for (TreeNode treeNode : getChildren()) {
            traverseResults += treeNode.traverseTree(depth + 1) + "\n";
        }
        if (traverseResults.length() > 0) {
            return traverseResults.substring(0, traverseResults.length() - 1);
        } else {
            return traverseResults;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }
}
