package com.zenjava.jfxflow.actvity;

import javafx.beans.property.BooleanProperty;

public interface Activatable
{
    void setActive(boolean active);

    boolean isActive();

    BooleanProperty activeProperty();
}
