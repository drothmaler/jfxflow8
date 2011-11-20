package com.zenjava.jfxflow.transition;

import javafx.geometry.Bounds;

public abstract class AbstractViewTransition implements ViewTransition
{
    public void setupBeforeAnimation(Bounds bounds)
    {
    }

    public void cleanupAfterAnimation()
    {
    }
}
