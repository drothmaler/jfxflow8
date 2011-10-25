package com.zenjava.jfxflow.navigation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultNavigationManager implements NavigationManager
{
    private static final Logger log = LoggerFactory.getLogger(DefaultNavigationManager.class);

    private List<Place> history;
    private Place currentPlace;
    private int currentPlaceInHistory;
    private List<NavigationListener> navigationListeners;

    public DefaultNavigationManager()
    {
        this.history = new ArrayList<Place>();
        this.navigationListeners = new ArrayList<NavigationListener>();
    }

    public List<Place> getHistory()
    {
        return history;
    }

    public Place getCurrentPlace()
    {
        return currentPlace;
    }

    public int getCurrentPlaceInHistory()
    {
        return currentPlaceInHistory;
    }

    public void goTo(Place place)
    {
        log.debug("Navigating to place '{}'", place);
        Place prevPlace = currentPlace;
        int prevPlaceInHistory = currentPlaceInHistory;
        if (currentPlaceInHistory + 1 < history.size())
        {
            history.subList(currentPlaceInHistory + 1, history.size()).clear();
        }
        this.history.add(place);
        this.currentPlaceInHistory = this.history.size() - 1;
        this.currentPlace = place;
        firePlaceUpdated(TransitionType.normal,
                prevPlaceInHistory, prevPlace, currentPlaceInHistory, currentPlace);
    }

    public void refresh()
    {
        firePlaceUpdated(TransitionType.reshow,
                currentPlaceInHistory, currentPlace, currentPlaceInHistory, currentPlace);
    }

    public void goBack()
    {
        if (currentPlaceInHistory > 0 && currentPlaceInHistory - 1 < history.size())
        {
            log.debug("Navigating back, current index = {}, history size = {}", currentPlaceInHistory, history.size());
            Place prevPlace = currentPlace;
            int prevPlaceInHistory = currentPlaceInHistory;
            this.currentPlaceInHistory--;
            this.currentPlace = history.get(this.currentPlaceInHistory);
            firePlaceUpdated(TransitionType.back,
                    prevPlaceInHistory, prevPlace, currentPlaceInHistory, currentPlace);
        }
    }

    public void goForward()
    {
        if (currentPlaceInHistory + 1 < history.size() && currentPlaceInHistory + 1 > 0)
        {
            log.debug("Navigating forward, current index = {}, history size = {}", currentPlaceInHistory, history.size());
            Place prevPlace = currentPlace;
            int prevPlaceInHistory = currentPlaceInHistory;
            this.currentPlaceInHistory++;
            firePlaceUpdated(TransitionType.forward,
                    prevPlaceInHistory, prevPlace, currentPlaceInHistory, currentPlace);
        }
    }

    public void addNavigationListener(NavigationListener listener)
    {
        navigationListeners.add(listener);
    }

    public void removeNavigationListener(NavigationListener listener)
    {
        navigationListeners.remove(listener);
    }

    protected void firePlaceUpdated(TransitionType transitionType,
                                    int prevPlaceInHistory, Place prevPlace,
                                    int currentPlaceInHistory, Place currentPlace)
    {
        NavigationEvent event = new NavigationEvent(this, transitionType,
                prevPlaceInHistory, prevPlace, currentPlaceInHistory, currentPlace);
        for (NavigationListener listener : navigationListeners)
        {
            listener.placeUpdated(event);
        }
    }
}
