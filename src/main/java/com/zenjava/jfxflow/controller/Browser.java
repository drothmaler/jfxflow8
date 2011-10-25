package com.zenjava.jfxflow.controller;

import com.zenjava.jfxflow.navigation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class Browser extends StackPane implements NavigationListener
{
    private NavigationManager navigationManager;
    private BooleanProperty busyProperty;
    private NavigationToolbar navigationToolbar;
    private BorderPane contentPane;
    private ControllerContainer controllerContainer;
    private Node glassPane;

    public Browser(String title, NavigationManager navigationManager, Place homePlace)
    {
        this.navigationManager = navigationManager;
        buildView(title, homePlace);
        this.navigationManager.addNavigationListener(this);
    }

    public void registerController(Class<? extends Place> placeType, Controller controller)
    {
        this.controllerContainer.registerController(placeType, controller);
    }

    public void setHomePlace(Place homePlace)
    {
        this.navigationToolbar.setHomePlace(homePlace);
    }

    public void placeUpdated(NavigationEvent event)
    {
        controllerContainer.showControllerForPlace(event.getCurrentPlace(), event.getTransitionType());
    }

    protected void buildView(String title, Place homePlace)
    {
        getStyleClass().add("browser");

        contentPane = new BorderPane();
        navigationToolbar = new NavigationToolbar(navigationManager, homePlace);
        contentPane.setTop(buildHeader(title, navigationToolbar));
        controllerContainer = new ControllerContainer();
        contentPane.setCenter(controllerContainer);
        getChildren().add(contentPane);

        glassPane = buildGlassPane();
        glassPane.setVisible(false);
        getChildren().add(glassPane);

        busyProperty = new SimpleBooleanProperty();
        glassPane.visibleProperty().bind(busyProperty);
        controllerContainer.currentControllerProperty().addListener(new ChangeListener<Controller>()
        {
            public void changed(ObservableValue<? extends Controller> observableValue,
                                Controller oldController, Controller newController)
            {
                busyProperty.unbind();
                if (newController != null)
                {
                    busyProperty.bind(newController.busyProperty());
                }
            }
        });
    }

    protected Node buildGlassPane()
    {
        BorderPane glassPane = new BorderPane();
        glassPane.getStyleClass().add("glass-pane");
        return glassPane;
    }

    protected Node buildHeader(String title, NavigationToolbar navigationToolbar)
    {
        StackPane header = new StackPane();
        header.getStyleClass().add("browser-header");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("browser-title");
        header.getChildren().add(titleLabel);
        header.getChildren().add(navigationToolbar);
        return header;
    }
}
