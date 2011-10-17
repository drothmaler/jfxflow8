package com.zenjava.demo.search;

import com.zenjava.jfxflow.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class SearchController extends AbstractController
{
    public static final String LOCATION = SearchController.class.getName();

    @FXML private Node rootNode;

    public Node getRootNode()
    {
        return rootNode;
    }

}
