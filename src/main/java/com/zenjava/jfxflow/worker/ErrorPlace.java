package com.zenjava.jfxflow.worker;

import com.zenjava.jfxflow.navigation.Place;

public class ErrorPlace implements Place
{
    private Throwable error;

    public ErrorPlace(Throwable error)
    {
        this.error = error;
    }

    public Throwable getError()
    {
        return error;
    }
}
