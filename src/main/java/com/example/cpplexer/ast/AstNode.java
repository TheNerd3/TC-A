package com.example.cpplexer.ast;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class AstNode {
    private final String label;
    private final List<AstNode> children = new ArrayList<>();

    public AstNode(String label) {
        this.label = label;
    }

    public AstNode addChild(AstNode child) {
        if (child != null) {
            children.add(child);
        }
        return this;
    }

    public AstNode addChildren(List<AstNode> nodes) {
        if (nodes != null) {
            for (AstNode node : nodes) {
                addChild(node);
            }
        }
        return this;
    }

    public DefaultMutableTreeNode toSwingNode() {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(label);
        for (AstNode child : children) {
            node.add(child.toSwingNode());
        }
        return node;
    }

    public String toIndentedString() {
        StringBuilder builder = new StringBuilder();
        appendIndented(builder, 0);
        return builder.toString();
    }

    private void appendIndented(StringBuilder builder, int depth) {
        builder.append("  ".repeat(Math.max(0, depth))).append(label).append(System.lineSeparator());
        for (AstNode child : children) {
            child.appendIndented(builder, depth + 1);
        }
    }
}