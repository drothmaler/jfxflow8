package com.zenjava.jfxflow.actvity;

import javafx.animation.Animation;
import javafx.beans.property.ReadOnlyObjectProperty;

public interface HasTransition
{
    ReadOnlyObjectProperty<Animation> currentTransitionProperty();

    default Animation getCurrentTransition()
    {
        return currentTransitionProperty().get();
    }
}
