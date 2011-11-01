package com.zenjava.jfxflow.worker;

import com.zenjava.jfxflow.navigation.NavigationManager;

public class DefaultErrorHandler implements ErrorHandler
{
    private NavigationManager navigationManager;

    public DefaultErrorHandler(NavigationManager navigationManager)
    {
        this.navigationManager = navigationManager;
    }

    public void handleError(Throwable error)
    {
        navigationManager.goTo(new ErrorPlace(error));
    }
}
