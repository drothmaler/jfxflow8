package com.zenjava.jfxflow.transition;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class FadeOutTransition extends AbstractViewTransition
{
    private Node targetNode;
    private Duration duration;

    public FadeOutTransition(Node targetNode, Duration duration)
    {
        this.targetNode = targetNode;
        this.duration = duration;
    }

    public Animation getAnimation()
    {
        FadeTransition fadeOut = new FadeTransition(duration, targetNode);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0);
        return fadeOut;
    }

    public void cleanupAfterAnimation()
    {
        targetNode.setOpacity(1);
    }
}
