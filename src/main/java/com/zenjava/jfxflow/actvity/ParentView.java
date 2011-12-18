package com.zenjava.jfxflow.actvity;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public interface ParentView<NodeType extends Node> extends View<NodeType>
{
    StackPane getChildArea();
}
