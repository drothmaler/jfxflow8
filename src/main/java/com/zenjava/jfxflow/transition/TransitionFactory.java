package com.zenjava.jfxflow.transition;

import com.zenjava.jfxflow.actvity.Activity;
import javafx.animation.Animation;
import javafx.scene.layout.StackPane;

public interface TransitionFactory
{
    Animation createTransition(StackPane contentArea, Activity fromActivity, Activity toActivity);
}
