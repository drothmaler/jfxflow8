package com.zenjava.jfxflow;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

public interface ControlManager
{
    void goTo(Location location);

    ObservableList<Location> getHistory();

    IntegerProperty currentPlaceInHistoryProperty();

    ObjectProperty<Controller> currentControllerProperty();

    void refresh();

    void goBack();

    void goForward();
}
