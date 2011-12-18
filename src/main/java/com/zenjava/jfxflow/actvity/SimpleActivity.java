package com.zenjava.jfxflow.actvity;

import javafx.scene.Node;

public class SimpleActivity extends AbstractActivity
{
    public SimpleActivity()
    {
    }

    public SimpleActivity(Node node)
    {
        this(new SimpleView(node));
    }

    public SimpleActivity(View view)
    {
        setView(view);
    }
}
