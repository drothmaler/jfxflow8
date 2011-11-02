package com.zenjava.jfxflow.controller;

import com.zenjava.jfxflow.navigation.Place;

public abstract class ParentPlace implements Place
{
    private Place subPlace;

    protected ParentPlace(Place subPlace)
    {
        this.subPlace = subPlace;
    }

    public Place getSubPlace()
    {
        return subPlace;
    }
}
