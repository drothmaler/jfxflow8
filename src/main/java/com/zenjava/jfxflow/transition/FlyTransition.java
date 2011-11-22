package com.zenjava.jfxflow.transition;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.util.Duration;

public class FlyTransition extends AbstractViewTransition
{
    public static FlyTransition createFlyIn(Node targetNode,
                                            Duration duration,
                                            HorizontalPosition horizontalStartPosition)
    {
        return createFlyIn(targetNode, duration, horizontalStartPosition, VerticalPosition.center);
    }

    public static FlyTransition createFlyIn(Node targetNode,
                                            Duration duration,
                                            VerticalPosition verticalStartPosition)
    {
        return createFlyIn(targetNode, duration, HorizontalPosition.center, verticalStartPosition);
    }

    public static FlyTransition createFlyIn(Node targetNode,
                                            Duration duration,
                                            HorizontalPosition horizontalStartPosition,
                                            VerticalPosition verticalStartPosition)
    {
        return new FlyTransition(targetNode, duration,
                horizontalStartPosition, verticalStartPosition,
                HorizontalPosition.center, VerticalPosition.center);
    }

    public static FlyTransition createFlyOut(Node targetNode,
                                             Duration duration,
                                             HorizontalPosition horizontalEndPosition)
    {
        return createFlyOut(targetNode, duration, horizontalEndPosition, VerticalPosition.center);
    }

    public static FlyTransition createFlyOut(Node targetNode,
                                             Duration duration,
                                             VerticalPosition verticalEndPosition)
    {
        return createFlyOut(targetNode, duration, HorizontalPosition.center, verticalEndPosition);
    }

    public static FlyTransition createFlyOut(Node targetNode,
                                             Duration duration,
                                             HorizontalPosition horizontalEndPosition,
                                             VerticalPosition verticalEndPosition)
    {
        return new FlyTransition(targetNode, duration,
                HorizontalPosition.center, VerticalPosition.center,
                horizontalEndPosition, verticalEndPosition);
    }

    private Node targetNode;
    private Duration duration;
    private HorizontalPosition horizontalStartPosition;
    private VerticalPosition verticalStartPosition;
    private HorizontalPosition horizontalEndPosition;
    private VerticalPosition verticalEndPosition;

    private double startY;
    private double startX;
    private double endX;
    private double endY;

    public FlyTransition(Node targetNode,
                         Duration duration,
                         HorizontalPosition horizontalStartPosition,
                         VerticalPosition verticalStartPosition,
                         HorizontalPosition horizontalEndPosition,
                         VerticalPosition verticalEndPosition)
    {
        this.targetNode = targetNode;
        this.duration = duration;
        this.horizontalStartPosition = horizontalStartPosition;
        this.verticalStartPosition = verticalStartPosition;
        this.horizontalEndPosition = horizontalEndPosition;
        this.verticalEndPosition = verticalEndPosition;
    }

    public void setupBeforeAnimation(Bounds bounds)
    {
        startX = getX(bounds, horizontalStartPosition);
        startY = getY(bounds, verticalStartPosition);
        endX = getX(bounds, horizontalEndPosition);
        endY = getY(bounds, verticalEndPosition);
        targetNode.setTranslateX(startX);
        targetNode.setTranslateY(startY);
    }

    public Animation getAnimation()
    {
        TranslateTransition translation = new TranslateTransition(duration, targetNode);
        translation.setFromX(startX);
        translation.setFromY(startY);
        translation.setToX(endX);
        translation.setToY(endY);
        return translation;
    }

    public void cleanupAfterAnimation()
    {
        targetNode.setTranslateX(0);
        targetNode.setTranslateY(0);
    }

    protected double getX(Bounds bounds, HorizontalPosition position)
    {
        switch (position)
        {
            case left:
                return -(bounds.getMaxX() - bounds.getMinX());
            case right:
                return bounds.getMaxX();
            default:
                return 0;
        }
    }

    protected double getY(Bounds bounds, VerticalPosition position)
    {
        switch (position)
        {
            case top:
                return -(bounds.getMaxY() - bounds.getMinY());
            case bottom:
                return bounds.getMaxY();
            default:
                return 0;
        }
    }

}
