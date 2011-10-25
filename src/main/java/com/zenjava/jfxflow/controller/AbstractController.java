package com.zenjava.jfxflow.controller;

import com.zenjava.jfxflow.navigation.Place;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController<PlaceType extends Place>
        implements Controller<PlaceType>, Initializable, HasFxmlLoadedView
{
    private Node view;
    private BooleanProperty busy;

    protected AbstractController()
    {
        busy = new SimpleBooleanProperty();
    }

    public void setView(Node view)
    {
        this.view = view;
    }

    public Node getView()
    {
        return view;
    }

    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public void activate(PlaceType place)
    {
    }

    public void deactivate()
    {
    }

    public BooleanProperty busyProperty()
    {
        return busy;
    }
}
