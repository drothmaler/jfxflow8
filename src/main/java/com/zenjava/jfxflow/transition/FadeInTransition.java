package com.zenjava.jfxflow.transition;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Duration;

public class FadeInTransition extends AbstractViewTransition
{
    private Node targetNode;
    private Duration duration;

    public FadeInTransition(Node targetNode, Duration duration)
    {
        this.targetNode = targetNode;
        this.duration = duration;
    }

    public void setupBeforeAnimation(Bounds bounds)
    {
        targetNode.setOpacity(0);
    }

    public Animation getAnimation()
    {
        FadeTransition fadeIn = new FadeTransition(duration, targetNode);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        return fadeIn;
    }
}
