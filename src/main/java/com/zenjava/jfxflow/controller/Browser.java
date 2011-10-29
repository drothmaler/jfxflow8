/*
 * Copyright (c) 2011, Daniel Zwolenski. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
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
