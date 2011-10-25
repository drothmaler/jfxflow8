package com.zenjava.jfxflow.controller;

import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.TransitionType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ControllerContainer extends StackPane
{
    private static final Logger log = LoggerFactory.getLogger(ControllerContainer.class);

    private ObjectProperty<Controller> currentController;
    private Map<Class, Controller> controllers;

    public ControllerContainer()
    {
        this.currentController = new SimpleObjectProperty<Controller>();
        this.controllers = new HashMap<Class, Controller>();
    }

    public void registerController(Class<? extends Place> placeType, Controller<? extends Place> controller)
    {
        this.controllers.put(placeType, controller);
    }

    public ReadOnlyObjectProperty<Controller> currentControllerProperty()
    {
        return currentController;
    }

    public Controller<?> getCurrentController()
    {
        return currentController.get();
    }

    @SuppressWarnings("unchecked")
    public void showControllerForPlace(Place place, TransitionType transitionType)
    {
        Controller<? extends Place> newController = null;
        if (place != null)
        {
            newController = controllers.get(place.getClass());
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
}
