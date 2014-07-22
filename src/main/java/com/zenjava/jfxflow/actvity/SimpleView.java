package com.zenjava.jfxflow.actvity;

import javafx.scene.Node;

public class SimpleView<NodeType extends Node> implements View<NodeType>
{
    private final NodeType node;

    public SimpleView(NodeType node)
    {
        this.node = node;
    }

    @Override
    public NodeType toNode()
    {
        return node;
    }
}
