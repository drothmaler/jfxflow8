package com.zenjava.jfxflow;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController<DataType> implements Controller<DataType>, Initializable
{
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public void activate(DataType data)
    {
    }

    public void deactivate()
    {
    }

}
