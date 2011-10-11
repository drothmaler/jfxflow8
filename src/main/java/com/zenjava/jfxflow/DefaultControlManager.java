package com.zenjava.jfxflow;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DefaultControlManager implements ControlManager
{
    private static final Logger log = LoggerFactory.getLogger(DefaultControlManager.class);

    private StackPane contentPane;
    private ObservableList<Location> history;
    private IntegerProperty currentPlaceInHistory;
    private Map<String, Controller<?>> controllers;
    private Controller currentController;

    public DefaultControlManager(StackPane contentPane)
    {
        this.contentPane = contentPane;
        this.history = FXCollections.observableArrayList();
        this.currentPlaceInHistory = new SimpleIntegerProperty();
        this.controllers = new HashMap<String, Controller<?>>();
    }

    public ObservableList<Location> getHistory()
    {
        return history;
    }

    public IntegerProperty getCurrentPlaceInHistory()
    {
        return currentPlaceInHistory;
    }

    public void register(String locationId, Controller controller)
    {
        controllers.put(locationId, controller);
    }

    @SuppressWarnings("unchecked")
    public void goTo(Location location)
    {
        log.debug("Navigating to {} (data={})", location.getId(), location.getData());
        Controller controller = controllers.get(location.getId());
        if (controller != null)
        {
            transition(currentController, controller, location.getData());
            int current = this.currentPlaceInHistory.get();
            if (current + 1 < history.size())
            {
                history.subList(current + 1, history.size()).clear();
            }
            this.history.add(location);
            this.currentPlaceInHistory.set(this.history.size() - 1);
            log.trace("Current index in history is now {}, size = {}", currentPlaceInHistory.get(), history.size());
        }
        else
        {
            throw new IllegalArgumentException(String.format(
                    "No controller registered for location: '%s'", location.getId()));
        }
    }

    @SuppressWarnings("unchecked")
    public void refresh()
    {
        if (currentController != null)
        {
            log.debug("Refreshing current controller");
            currentController.deactivate();
            currentController.activate(history.get(currentPlaceInHistory.get()).getData());
        }
    }

    public void goBack()
    {
        int current = currentPlaceInHistory.get();
        if (current > 0 && current - 1 < history.size())
        {
            log.debug("Navigating back, current index = {}, history size = {}", current, history.size());
            Location prevLocation = history.get(current - 1);
            Controller prevController = controllers.get(prevLocation.getId());
            transition(currentController, prevController, prevLocation.getData());
            this.currentPlaceInHistory.set(current - 1);
            log.trace("Current index in history is now {}, size = {}", currentPlaceInHistory.get(), history.size());
        }
    }

    public void goForward()
    {
        int current = currentPlaceInHistory.get();
        if (current + 1 < history.size() && current + 1 > 0)
        {
            log.debug("Navigating forward, current index = {}, history size = {}", current, history.size());
            Location nextLocation = history.get(current + 1);
            Controller nextController = controllers.get(nextLocation.getId());
            transition(currentController, nextController, nextLocation.getData());
            this.currentPlaceInHistory.set(current + 1);
            log.trace("Current index in history is now {}, size = {}", currentPlaceInHistory.get(), history.size());
        }
    }

    @SuppressWarnings("unchecked")
    protected void transition(Controller from, Controller to, Object newData)
    {
        if (from != null)
        {
            contentPane.getChildren().remove(from.getRootNode());
            from.deactivate();
        }
        this.currentController = to;
        if (to != null)
        {
            to.activate(newData);
            contentPane.getChildren().add(to.getRootNode());
        }
    }
}
