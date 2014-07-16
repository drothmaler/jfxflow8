package com.zenjava.jfxflow.actvity;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class SimpleParentView extends BorderPane implements ParentView<BorderPane>
{
    private final StackPane childArea = new StackPane();

    public SimpleParentView()
    {
        childArea.getStyleClass().add("child-area");
        setCenter(childArea);
    }

    public BorderPane toNode()
    {
        return this;
    }

    public StackPane getChildArea()
    {
        return childArea;
    }
}
