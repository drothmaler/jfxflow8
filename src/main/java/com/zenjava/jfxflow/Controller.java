package com.zenjava.jfxflow;

import javafx.scene.Node;

public interface Controller<DataType>
{
    Node getRootNode();

    void activate(DataType data);

    void deactivate();
}
