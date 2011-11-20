package com.zenjava.jfxflow.navigation;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Provides a default implementation of a NavigationManager with standard support for managing the current place as
 * well as the back and forward history. This class can be used as is for standard navigation support, or can be
 * sub-classed for specific navigation requirements.
 */
public class DefaultNavigationManager implements NavigationManager
{
    private ObjectProperty<Place> currentPlace;
    private ObservableList<Place> backHistory;
    private ObservableList<Place> forwardHistory;

    public DefaultNavigationManager()
    {
        this.currentPlace = new SimpleObjectProperty<Place>();
        this.backHistory = FXCollections.observableArrayList();
        this.forwardHistory = FXCollections.observableArrayList();
    }

    public void goTo(Place place)
    {
        this.forwardHistory.clear();
        Place currentPlace = this.currentPlace.get();
        if (currentPlace != null)
        {
            backHistory.add(backHistory.size(), currentPlace);
        }
        this.currentPlace.set(place);
    }

    public void goBack()
    {
        if (backHistory.size() > 0)
        {
            Place currentPlace = this.currentPlace.get();
            if (currentPlace != null)
            {
                forwardHistory.add(0, currentPlace);
            }

            Place backPlace = backHistory.remove(backHistory.size() - 1);
            this.currentPlace.set(backPlace);
        }
    }

    public void goForward()
    {
        if (forwardHistory.size() > 0)
        {
            Place currentPlace = this.currentPlace.get();
            if (currentPlace != null)
            {
                backHistory.add(backHistory.size(), currentPlace);
            }

            Place backPlace = forwardHistory.remove(0);
            this.currentPlace.set(backPlace);
        }
    }

    public Place getCurrentPlace()
    {
        return this.currentPlace.get();
    }

    public void setCurrentPlace(Place place)
    {
        this.currentPlace.set(place);
    }

    public ObjectProperty<Place> currentPlaceProperty()
    {
        return this.currentPlace;
    }

    public ObservableList<Place> getBackHistory()
    {
        return this.backHistory;
    }

    public ObservableList<Place> getForwardHistory()
    {
        return this.forwardHistory;
    }
}
