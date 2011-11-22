package com.zenjava.jfxflow.control;

import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

// todo use Control + skin
public class PlaceHyperlink extends NavigationHyperlink
{
    private ObjectProperty<Place> place;

    public PlaceHyperlink()
    {
        this(null, null, null, null);
    }

    public PlaceHyperlink(Place place)
    {
        this(null, null, null, place);
    }

    public PlaceHyperlink(NavigationManager navigationManager)
    {
        this(null, null, navigationManager, null);
    }

    public PlaceHyperlink(NavigationManager navigationManager, Place place)
    {
        this(null, null, navigationManager, place);
    }

    public PlaceHyperlink(String label)
    {
        this(label, null, null, null);
    }

    public PlaceHyperlink(String label, Place place)
    {
        this(label, null, null, place);
    }

    public PlaceHyperlink(String label, NavigationManager navigationManager)
    {
        this(label, null, navigationManager, null);
    }

    public PlaceHyperlink(String label, NavigationManager navigationManager, Place place)
    {
        this(label, null, navigationManager, place);
    }

    public PlaceHyperlink(Node graphic)
    {
        this(null, graphic, null, null);
    }

    public PlaceHyperlink(Node graphic, Place place)
    {
        this(null, graphic, null, place);
    }

    public PlaceHyperlink(Node graphic, NavigationManager navigationManager)
    {
        this(null, graphic, navigationManager, null);
    }

    public PlaceHyperlink(Node graphic, NavigationManager navigationManager, Place place)
    {
        this(null, graphic, navigationManager, place);
    }

    public PlaceHyperlink(String label, Node graphic)
    {
        this(label, graphic, null, null);
    }

    public PlaceHyperlink(String label, Node graphic, Place place)
    {
        this(label, graphic, null, place);
    }

    public PlaceHyperlink(String label, Node graphic, final NavigationManager navigationManager, Place place)
    {
        super(label, graphic, navigationManager);
        getStyleClass().add("place-hyperlink");
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
