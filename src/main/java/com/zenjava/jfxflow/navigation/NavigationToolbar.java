package com.zenjava.jfxflow.navigation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.List;

public class NavigationToolbar extends HBox implements NavigationListener
{
    protected Button homeButton;
    protected Button refreshButton;
    protected Button backButton;
    protected Button forwardButton;
    protected NavigationManager navigationManager;
    protected Place homePlace;

    public NavigationToolbar(NavigationManager navigationManager, Place homePlace)
    {
        this.navigationManager = navigationManager;
        this.homePlace = homePlace;
        buildView();

        this.navigationManager.addNavigationListener(this);
        updateButtonStates();
    }

    public void setHomePlace(Place homePlace)
    {
        this.homePlace = homePlace;
    }

    public void placeUpdated(NavigationEvent event)
    {
        updateButtonStates();
    }

    protected void homeSelected()
    {
        navigationManager.goTo(homePlace);
    }

    protected void refreshSelected()
    {
        navigationManager.refresh();
    }

    protected void backSelected()
    {
        navigationManager.goBack();
    }

    protected void forwardSelected()
    {
        navigationManager.goForward();
    }

    protected void buildView()
    {
        setSpacing(4);
        getStyleClass().add("navigation-toolbar");

        homeButton = new Button("Home");
        homeButton.getStyleClass().add("home");
        homeButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                homeSelected();
            }
        });
        getChildren().add(homeButton);

        refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("refresh");
        refreshButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                refreshSelected();
            }
        });
        getChildren().add(refreshButton);

        backButton = new Button("Back");
        backButton.getStyleClass().add("back");
        backButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                backSelected();
            }
        });
        getChildren().add(backButton);

        forwardButton = new Button("Forward");
        forwardButton.getStyleClass().add("forward");
        forwardButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                forwardSelected();
            }
        });
        getChildren().add(forwardButton);
    }

    protected void updateButtonStates()
    {
        List<Place> history = navigationManager.getHistory();
        int currentPlaceInHistory = navigationManager.getCurrentPlaceInHistory();
        backButton.setDisable(currentPlaceInHistory == 0);
        forwardButton.setDisable(currentPlaceInHistory >= history.size() - 1);
    }
}
