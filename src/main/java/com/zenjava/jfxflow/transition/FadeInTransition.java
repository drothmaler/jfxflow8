package com.zenjava.jfxflow.transition;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Duration;

public class FadeInTransition extends AbstractViewTransition
{
    private Node targetNode;

    public FadeInTransition(Node targetNode)
    {
        this.targetNode = targetNode;
    }

    public void setupBeforeAnimation(Bounds bounds)
    {
        targetNode.setOpacity(0);
    }

    public Animation getAnimation()
    {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), targetNode);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        return fadeIn;
    }
}
