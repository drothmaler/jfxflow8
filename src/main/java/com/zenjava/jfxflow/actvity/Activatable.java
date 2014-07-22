package com.zenjava.jfxflow.actvity;

import javafx.beans.property.BooleanProperty;

public interface Activatable
{

    default void setActive(boolean active)
    {
        this.activeProperty().set(active);
    }

    default boolean isActive()
    {
        return this.activeProperty().get();
    }

    BooleanProperty activeProperty();
}
