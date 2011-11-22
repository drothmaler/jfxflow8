package com.zenjava.jfxflow.control;

import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

// todo use Control + skin
public class PlaceButton extends NavigationButton
{
    private ObjectProperty<Place> place;

    public PlaceButton()
    {
        this(null, null, null, null);
    }

    public PlaceButton(Place place)
    {
        this(null, null, null, place);
    }

    public PlaceButton(NavigationManager navigationManager)
    {
        this(null, null, navigationManager, null);
    }

    public PlaceButton(NavigationManager navigationManager, Place place)
    {
        this(null, null, navigationManager, place);
    }

    public PlaceButton(String label)
    {
        this(label, null, null, null);
    }

    public PlaceButton(String label, Place place)
    {
        this(label, null, null, place);
    }

    public PlaceButton(String label, NavigationManager navigationManager)
    {
        this(label, null, navigationManager, null);
    }

    public PlaceButton(String label, NavigationManager navigationManager, Place place)
    {
        this(label, null, navigationManager, place);
    }

    public PlaceButton(Node graphic)
    {
        this(null, graphic, null, null);
    }

    public PlaceButton(Node graphic, Place place)
    {
        this(null, graphic, null, place);
    }

    public PlaceButton(Node graphic, NavigationManager navigationManager)
    {
        this(null, graphic, navigationManager, null);
    }

    public PlaceButton(Node graphic, NavigationManager navigationManager, Place place)
    {
        this(null, graphic, navigationManager, place);
    }

    public PlaceButton(String label, Node graphic)
    {
        this(label, graphic, null, null);
    }

    public PlaceButton(String label, Node graphic, Place place)
    {
        this(label, graphic, null, place);
    }

    public PlaceButton(String label, Node graphic, final NavigationManager navigationManager, Place place)
    {
        super(label, graphic, navigationManager);
        getStyleClass().add("place-button");
        this.place = new SimpleObjectProperty<Place>(place);
    }

    public ObjectProperty<Place> placeProperty()
    {
        return place;
    }

    public Place getPlace()
    {
        return place.get();
    }

    public void setPlace(Place place)
    {
        this.place.set(place);
    }

    protected void doAction()
    {
        getNavigationManager().goTo(place.get());
    }
}
