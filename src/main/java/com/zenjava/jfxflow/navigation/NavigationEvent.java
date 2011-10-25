package com.zenjava.jfxflow.navigation;

public class NavigationEvent
{
    private NavigationManager source;
    private TransitionType transitionType;
    private int previousPlaceInHistory;
    private Place previousPlace;
    private int currentPlaceInHistory;
    private Place currentPlace;

    public NavigationEvent(NavigationManager source,
                           TransitionType transitionType,
                           int previousPlaceInHistory, Place previousPlace,
                           int currentPlaceInHistory, Place currentPlace)
    {
        this.source = source;
        this.transitionType = transitionType;
        this.previousPlaceInHistory = previousPlaceInHistory;
        this.previousPlace = previousPlace;
        this.currentPlaceInHistory = currentPlaceInHistory;
        this.currentPlace = currentPlace;
    }

    public NavigationManager getSource()
    {
        return source;
    }

    public TransitionType getTransitionType()
    {
        return transitionType;
    }

    public int getPreviousPlaceInHistory()
    {
        return previousPlaceInHistory;
    }

    public Place getPreviousPlace()
    {
        return previousPlace;
    }

    public int getCurrentPlaceInHistory()
    {
        return currentPlaceInHistory;
    }

    public Place getCurrentPlace()
    {
        return currentPlace;
    }
}
