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

import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.TransitionType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ControllerContainer extends StackPane
{
    private static final Logger log = LoggerFactory.getLogger(ControllerContainer.class);

    private ObjectProperty<Controller<? extends Node, ? extends Place>> currentController;
    private Map<Class, Controller<? extends Node, ? extends Place>> controllers;

    public ControllerContainer()
    {
        this.currentController = new SimpleObjectProperty<Controller<? extends Node, ? extends Place>>();
        this.controllers = new HashMap<Class, Controller<? extends Node, ? extends Place>>();
    }

    public void registerController(Class<? extends Place> placeType, Controller<? extends Node, ? extends Place> controller)
    {
        this.controllers.put(placeType, controller);
    }

    public ReadOnlyObjectProperty<Controller<? extends Node, ? extends Place>> currentControllerProperty()
    {
        return currentController;
    }

    public Controller<? extends Node, ? extends Place> getCurrentController()
    {
        return currentController.get();
    }

    @SuppressWarnings("unchecked")
    public void showControllerForPlace(Place place, TransitionType transitionType)
    {
        Controller<? extends Node, ? extends Place> newController = null;
        if (place != null)
        {
            newController = lookupController(place.getClass());
        }
        transition(currentController.get(), newController, place, transitionType);
    }

    @SuppressWarnings("unchecked")
    protected void transition(Controller oldController, Controller newController,
                              Place newPlace, TransitionType transitionType)
    {
        log.debug("Transitioning between controllers, from {} to {}", oldController, newController);

        if (oldController != null)
        {
            oldController.deactivate();
            getChildren().remove(oldController.getView());
        }

        currentController.set(newController);
        if (newController != null)
        {
            newController.activate(newPlace);
            getChildren().add(newController.getView());
        }
    }

    protected Controller<? extends Node, ? extends Place> lookupController(Class placeType)
    {
        Controller<? extends Node, ? extends Place> controller = controllers.get(placeType);
        while (controller == null && placeType.getSuperclass() != null)
        {
            placeType = placeType.getSuperclass();
            controller = controllers.get(placeType);
        }
        return controller;
    }
}
