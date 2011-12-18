package com.zenjava.jfxflow.actvity;

import javafx.scene.Node;

public interface View<NodeType extends Node>
{
    NodeType toNode();
}
