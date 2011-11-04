package com.zenjava.jfxflow.controller;

import com.zenjava.jfxflow.navigation.NavigationEvent;
import com.zenjava.jfxflow.navigation.NavigationListener;
import com.zenjava.jfxflow.navigation.Place;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ParentController<ViewType extends Node, PlaceType extends Place>
        extends AbstractController<ViewType, PlaceType> implements NavigationListener
{
    private static final Logger log = LoggerFactory.getLogger(ParentController.class);

    private StackPane contentPane;
    private ObjectProperty<Controller<? extends Node, ? extends Place>> currentController;
    private Map<Class, Controller<? extends Node, ? extends Place>> controllers;

    protected ParentController()
    {
        this(null);
    }

    public ParentController(StackPane contentPane)
    {
        this.contentPane = contentPane;
        this.currentController = new SimpleObjectProperty<Controller<? extends Node, ? extends Place>>();
        this.controllers = new HashMap<Class, Controller<? extends Node, ? extends Place>>();
    }

    public void setContentPane(StackPane contentPane)
    {
        this.contentPane = contentPane;
    }

    public void registerController(Class<? extends Place> placeType, Controller<? extends Node, ? extends Place> controller)
    {
        this.controllers.put(placeType, controller);

        controller.busyProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean1)
            {
                for (Controller<? extends Node, ? extends Place> controller : controllers.values())
                {
                    if (controller.isBusy())
                    {
                        setBusy(true);
                        return;
                    }
                }
                setBusy(false);
            }
        });
    }

    public void activate(PlaceType place)
    {
        super.activate(place);
        Place subPlace = place;
        if (place instanceof ParentPlace)
        {
            ParentPlace parentPlace = (ParentPlace) place;
            subPlace = parentPlace.getSubPlace();
        }
        showPlace(subPlace);
    }

    public void deactivate()
    {
        super.deactivate();
        Controller<? extends Node, ? extends Place> controller = this.currentController.get();
        if (controller != null)
        {
            controller.deactivate();
        }
    }

    public ReadOnlyObjectProperty<Controller<? extends Node, ? extends Place>> currentControllerProperty()
    {
        return currentController;
    }

    public Controller<? extends Node, ? extends Place> getCurrentController()
    {
        return currentController.get();
    }

    public void placeUpdated(NavigationEvent event)
    {
        showPlace(event.getCurrentPlace());
    }

    @SuppressWarnings("unchecked")
    public void showPlace(Place place)
    {
        if (contentPane == null)
        {
            throw new IllegalStateException("You must call 'setContentPane' before using this ParentController");
        }

        Controller oldController = currentController.get();
        if (oldController != null)
        {
            oldController.deactivate();
            contentPane.getChildren().remove(oldController.getView());
        }

        Controller newController = lookupController(place.getClass());
        currentController.set(newController);
        newController.activate(place);
        contentPane.getChildren().add(newController.getView());
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
