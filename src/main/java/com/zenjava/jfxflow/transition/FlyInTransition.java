package com.zenjava.jfxflow.transition;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Duration;

public class FlyInTransition extends AbstractViewTransition
{
    private Node targetNode;
    private double startY;

    public FlyInTransition(Node targetNode)
    {
        this.targetNode = targetNode;
    }

    public void setupBeforeAnimation(Bounds bounds)
    {
        startY = bounds.getMaxY();
        targetNode.setTranslateY(startY);
    }

    public Animation getAnimation()
    {
        TranslateTransition translation = new TranslateTransition(Duration.millis(500), targetNode);
        translation.setFromY(startY);
        translation.setToY(0);
        return translation;
    }
}
