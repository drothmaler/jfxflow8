package com.zenjava.demo.home;

import com.zenjava.demo.search.SearchController;
import com.zenjava.jfxflow.AbstractController;
import com.zenjava.jfxflow.ControlManager;
import com.zenjava.jfxflow.Location;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class HomeController extends AbstractController
{
    public static final String LOCATION = HomeController.class.getName();

    @FXML private Node rootNode;

    private ControlManager controlManager;

    public Node getRootNode()
    {
        return rootNode;
    }

    public void setControlManager(ControlManager controlManager)
    {
        this.controlManager = controlManager;
    }

    public void search(ActionEvent event)
    {
        controlManager.goTo(new Location(SearchController.LOCATION));
    }

    public void add(ActionEvent event)
    {
    }
}
