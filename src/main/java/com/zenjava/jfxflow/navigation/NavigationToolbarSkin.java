package com.zenjava.jfxflow.navigation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.layout.HBox;

public class NavigationToolbarSkin implements Skin
{
    private NavigationToolbar navigationToolbar;
    private HBox root;
    private Button homeButton;
    private Button refreshButton;
    private Button backButton;
    private Button forwardButton;

    public NavigationToolbarSkin(NavigationToolbar navigationToolbar)
    {
        this.navigationToolbar = navigationToolbar;
        buildView();
    }

    public Skinnable getSkinnable()
    {
        return navigationToolbar;
    }

    public Node getNode()
    {
        return root;
    }

    public void dispose()
    {
        homeButton.disableProperty().unbind();
        refreshButton.disableProperty().unbind();
        backButton.disableProperty().unbind();
        forwardButton.disableProperty().unbind();
    }

    protected void buildView()
    {
        root = new HBox();
        root.spacingProperty().bind(navigationToolbar.spacingProperty());

        homeButton = new Button("Home");
        homeButton.getStyleClass().add("home");
        homeButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                navigationToolbar.homeSelected();
            }
        });
        homeButton.disableProperty().bind(navigationToolbar.homeAllowedProperty().not());
        root.getChildren().add(homeButton);

        refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("refresh");
        refreshButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                navigationToolbar.refreshSelected();
            }
        });
        refreshButton.disableProperty().bind(navigationToolbar.refreshAllowedProperty().not());
        root.getChildren().add(refreshButton);

        backButton = new Button("Back");
        backButton.getStyleClass().add("back");
        backButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                navigationToolbar.backSelected();
            }
        });
        backButton.disableProperty().bind(navigationToolbar.backAllowedProperty().not());
        root.getChildren().add(backButton);

        forwardButton = new Button("Forward");
        forwardButton.getStyleClass().add("forward");
        forwardButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                navigationToolbar.forwardSelected();
            }
        });
        forwardButton.disableProperty().bind(navigationToolbar.forwardAllowedProperty().not());
        root.getChildren().add(forwardButton);
    }

}
