package com.zenjava.jfxflow.navigation;

import javafx.util.Builder;

public class PlaceBuilder implements Builder<Place>
{
    public static PlaceBuilder create()
    {
        return new PlaceBuilder();
    }

    private Place place;

    public PlaceBuilder()
    {
        this(null);
    }

    public PlaceBuilder(String placeName)
    {
        this.place = new Place(placeName);
    }

    public PlaceBuilder name(String name)
    {
        place.setName(name);
        return this;
    }

    public PlaceBuilder parameter(String name, Object value)
    {
        this.place.getParameters().put(name, value);
        return this;
    }

    public Place build()
    {
        return place;
    }
}
