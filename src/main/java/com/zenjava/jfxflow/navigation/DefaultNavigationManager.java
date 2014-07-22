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
    private final ObjectProperty<Place> currentPlace = new SimpleObjectProperty<>();
    private final ObservableList<Place> backHistory = FXCollections.observableArrayList();
    private final ObservableList<Place> forwardHistory = FXCollections.observableArrayList();

    @Override
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

    @Override
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

    @Override
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

    @Override
    public ObjectProperty<Place> currentPlaceProperty()
    {
        return this.currentPlace;
    }

    @Override
    public ObservableList<Place> getBackHistory()
    {
        return this.backHistory;
    }

    @Override
    public ObservableList<Place> getForwardHistory()
    {
        return this.forwardHistory;
    }
}
