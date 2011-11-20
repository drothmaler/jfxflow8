package com.zenjava.jfxflow.actvity;

import javafx.scene.Node;

public interface HasNode<NodeType extends Node>
{
    NodeType getNode();
}
