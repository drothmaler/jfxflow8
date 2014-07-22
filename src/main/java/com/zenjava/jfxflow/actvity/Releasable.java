package com.zenjava.jfxflow.actvity;

import javafx.beans.property.ReadOnlyBooleanProperty;

public interface Releasable
{
    void release();

    ReadOnlyBooleanProperty releasedProperty();

    default boolean isReleased()
    {
        return releasedProperty().get();
    }
}
