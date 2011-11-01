/*
 * Copyright (c) 2011, Daniel Zwolenski. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or any later version.
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

import com.zenjava.jfxflow.navigation.NavigationEvent;
import com.zenjava.jfxflow.navigation.NavigationListener;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.worker.DefaultErrorController;
import com.zenjava.jfxflow.worker.ErrorPlace;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.Control;

public class Browser extends Control implements NavigationListener
{
    private ObjectProperty<NavigationManager> navigationManager;
    private ObservableMap<Class<? extends Place>, Controller> controllers;
    private BooleanProperty busy;
    private StringProperty title;
    private ObjectProperty<Place> homePlace;
    private ControllerContainer controllerContainer;
    private ObservableList<Node> dockBarItems;

    public Browser(String title)
    {
        this();
        this.title.set(title);
    }

    public Browser(String title, NavigationManager navigationManager, Place homePlace)
    {
        this();
        this.title.set(title);
        this.navigationManager.set(navigationManager);
        this.homePlace.set(homePlace);
        registerController(ErrorPlace.class, new DefaultErrorController());
    }

    public Browser()
    {
        getStyleClass().add("browser");

        this.navigationManager = new SimpleObjectProperty<NavigationManager>();
        this.controllers = FXCollections.observableHashMap();
        this.busy = new SimpleBooleanProperty(false);
        this.title = new SimpleStringProperty();
        this.homePlace = new SimpleObjectProperty<Place>();
        this.dockBarItems = FXCollections.observableArrayList();

        // todo: this should probably be part of the skin
        this.controllerContainer = new ControllerContainer();
        this.controllerContainer.getStyleClass().add("browser-content");
        controllerContainer.currentControllerProperty().addListener(new ChangeListener<Controller>()
        {
            public void changed(ObservableValue<? extends Controller> observableValue,
                                Controller oldController, Controller newController)
            {
                busy.unbind();
                busy.set(false);
                if (newController != null)
                {
                    busy.bind(newController.busyProperty());
                }
            }
        });

        this.navigationManager.addListener(new ChangeListener<NavigationManager>()
        {
            public void changed(ObservableValue<? extends NavigationManager> source,
                                NavigationManager oldNavigationManager,
                                NavigationManager newNavigationManager)
            {
                if (oldNavigationManager != null)
                {
                    oldNavigationManager.removeNavigationListener(Browser.this);
                }
                if (newNavigationManager != null)
                {
                    newNavigationManager.addNavigationListener(Browser.this);
                }
            }
        });
    }

    public void registerController(Class<? extends Place> placeType, Controller<? extends Node, ? extends Place> controller)
    {
        controllerContainer.registerController(placeType, controller);
    }

    public ObservableList<Node> getDockBarItems()
    {
        return dockBarItems;
    }

    public ControllerContainer getControllerContainer()
    {
        return controllerContainer;
    }

    public ObjectProperty<NavigationManager> navigationManagerProperty()
    {
        return navigationManager;
    }

    public NavigationManager getNavigationManager()
    {
        return navigationManager.get();
    }

    public void setNavigationManager(NavigationManager navigationManager)
    {
        this.navigationManager.set(navigationManager);
    }

    public ObservableMap<Class<? extends Place>, Controller> getControllers()
    {
        return controllers;
    }

    public void setControllers(ObservableMap<Class<? extends Place>, Controller> controllers)
    {
        this.controllers = controllers;
    }

    public BooleanProperty busyProperty()
    {
        return busy;
    }

    public void setBusy(boolean busy)
    {
        this.busy.set(busy);
    }

    public boolean getBusy()
    {
        return this.busy.get();
    }

    public StringProperty titleProperty()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title.set(title);
    }

    public String getTitle()
    {
        return this.title.get();
    }

    public ObservableObjectValue<Place> homePlaceProperty()
    {
        return homePlace;
    }

    public void setHomePlace(Place homePlace)
    {
        this.homePlace.set(homePlace);
    }

    public Place getHomePlace()
    {
        return this.homePlace.get();
    }

    public void placeUpdated(NavigationEvent event)
    {
        controllerContainer.showControllerForPlace(event.getCurrentPlace(), event.getTransitionType());
    }

    protected String getUserAgentStylesheet()
    {
        return "styles/jfxflow-browser.css";
    }
}
