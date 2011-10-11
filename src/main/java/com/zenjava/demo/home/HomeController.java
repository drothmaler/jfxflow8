package com.zenjava.demo.home;

import com.zenjava.jfxflow.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class HomeController extends AbstractController
{
    public static final String LOCATION = HomeController.class.getName();

    @FXML private Node rootNode;

    public Node getRootNode()
    {
        return rootNode;
    }
}
