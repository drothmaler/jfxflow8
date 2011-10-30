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
package com.zenjava.jfxflow.navigation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;

import java.util.List;

public class NavigationToolbar extends Control implements NavigationListener
{
    private BooleanProperty homeAllowed;
    private BooleanProperty refreshAllowed;
    private BooleanProperty backAllowed;
    private BooleanProperty forwardAllowed;
    private ObjectProperty<NavigationManager> navigationManager;
    private ObjectProperty<Place> homePlace;

    public NavigationToolbar(NavigationManager navigationManager, Place homePlace)
    {
        this();
        this.navigationManager.set(navigationManager);
        this.homePlace.set(homePlace);
    }

    public NavigationToolbar()
    {
        getStyleClass().add("navigation-toolbar");

        this.navigationManager = new SimpleObjectProperty<NavigationManager>();
        this.homePlace = new SimpleObjectProperty<Place>();
        this.homeAllowed = new SimpleBooleanProperty();
        this.refreshAllowed = new SimpleBooleanProperty();
        this.backAllowed = new SimpleBooleanProperty();
        this.forwardAllowed = new SimpleBooleanProperty();

        this.navigationManager.addListener(new ChangeListener<NavigationManager>()
        {
            public void changed(ObservableValue<? extends NavigationManager> source,
                                NavigationManager oldNavigationManager,
                                NavigationManager newNavigationManager)
            {
                if (oldNavigationManager != null)
                {
                    oldNavigationManager.removeNavigationListener(NavigationToolbar.this);
                }
                if (newNavigationManager != null)
                {
                    newNavigationManager.addNavigationListener(NavigationToolbar.this);
                }
            }
        });
        updateNavigationStates();
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


    public ObjectProperty<Place> homePlaceProperty()
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

    public BooleanProperty homeAllowedProperty()
    {
        return homeAllowed;
    }

    public boolean isHomeAllowed()
    {
        return homeAllowed.get();
    }

    public void setHomeAllowed(boolean homeAllowed)
    {
        this.homeAllowed.set(homeAllowed);
    }

    public BooleanProperty refreshAllowedProperty()
    {
        return refreshAllowed;
    }

    public boolean isRefreshAllowed()
    {
        return refreshAllowed.get();
    }

    public void setRefreshAllowed(boolean refreshAllowed)
    {
        this.refreshAllowed.set(refreshAllowed);
    }

    public BooleanProperty backAllowedProperty()
    {
        return backAllowed;
    }

    public boolean isBackAllowed()
    {
        return backAllowed.get();
    }

    public void setBackAllowed(boolean backAllowed)
    {
        this.backAllowed.set(backAllowed);
    }

    public BooleanProperty forwardAllowedProperty()
    {
        return forwardAllowed;
    }

    public boolean isForwardAllowed()
    {
        return forwardAllowed.get();
    }

    public void setForwardAllowed(boolean forwardAllowed)
    {
        this.forwardAllowed.set(forwardAllowed);
    }

    public void placeUpdated(NavigationEvent event)
    {
        updateNavigationStates();
    }

    void homeSelected()
    {
        getNavigationManager().goTo(getHomePlace());
    }

    void refreshSelected()
    {
        getNavigationManager().refresh();
    }

    void backSelected()
    {
        getNavigationManager().goBack();
    }

    void forwardSelected()
    {
        getNavigationManager().goForward();
    }

    protected String getUserAgentStylesheet()
    {
        return "styles/jfxflow-toolbar.css";
    }

    protected void updateNavigationStates()
    {
        NavigationManager navigationManager = getNavigationManager();
        if (navigationManager != null)
        {
            List<Place> history = navigationManager.getHistory();
            int currentPlaceInHistory = navigationManager.getCurrentPlaceInHistory();
            homeAllowed.set(homePlace.get() != null);
            refreshAllowed.set(currentPlaceInHistory >= 0);
            backAllowed.set(currentPlaceInHistory > 0);
            forwardAllowed.set(currentPlaceInHistory < history.size() - 1);
        }
        else
        {
            homeAllowed.set(false);
            refreshAllowed.set(false);
            backAllowed.set(false);
            forwardAllowed.set(false);
        }
    }
}
