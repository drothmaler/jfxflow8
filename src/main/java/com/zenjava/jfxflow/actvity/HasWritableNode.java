package com.zenjava.jfxflow.actvity;

import javafx.scene.Node;

public interface HasWritableNode<NodeType extends Node> extends HasNode<NodeType>
{
    void setNode(NodeType node);
}
