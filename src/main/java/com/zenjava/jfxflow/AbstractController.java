package com.zenjava.jfxflow;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController<DataType> implements Controller<DataType>, Initializable
{
    private BooleanProperty busy;

    protected AbstractController()
    {
        busy = new SimpleBooleanProperty();
    }

    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public void activate(DataType data)
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
