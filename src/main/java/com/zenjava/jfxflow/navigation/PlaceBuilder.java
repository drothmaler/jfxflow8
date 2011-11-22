package com.zenjava.jfxflow.navigation;

public class PlaceBuilder
{
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
