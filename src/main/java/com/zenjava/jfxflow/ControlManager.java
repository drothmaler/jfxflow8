package com.zenjava.jfxflow;

import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;

public interface ControlManager
{
    void goTo(Location location);

    ObservableList<Location> getHistory();

    IntegerProperty getCurrentPlaceInHistory();

    void refresh();

    void goBack();

    void goForward();
}
