package com.zenjava.jfxflow.controller;

import com.zenjava.jfxflow.navigation.Place;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;

public interface Controller<PlaceType extends Place>
{
    Node getView();

    void activate(PlaceType place);

    void deactivate();

    BooleanProperty busyProperty();
}
