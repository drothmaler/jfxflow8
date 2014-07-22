package com.zenjava.jfxflow.transition;

import javafx.geometry.Bounds;

public abstract class AbstractViewTransition implements ViewTransition
{
    @Override
    public void setupBeforeAnimation(Bounds bounds)
    {
    }

    @Override
    public void cleanupAfterAnimation()
    {
    }
}
