package com.zenjava.jfxflow.error;

import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.PlaceBuilder;
import com.zenjava.jfxflow.worker.UnhandledWorkerException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DefaultErrorHandler implements ErrorHandler
{
    public static final String ERROR_PLACE_NAME = "error";

    private ObjectProperty<NavigationManager> navigationManager;

    public DefaultErrorHandler()
    {
        this(null);
    }

    public DefaultErrorHandler(NavigationManager navigationManager)
    {
        this.navigationManager = new SimpleObjectProperty<NavigationManager>(navigationManager);
    }

    public ObjectProperty<NavigationManager> navigationManagerProperty()
    {
        return navigationManager;
    }

    public NavigationManager getNavigationManager()
    {
        return navigationManager.get();
    }

    public void setNavigationManager(NavigationManager navigationManager)
    {
        this.navigationManager.set(navigationManager);
    }

    public void handleError(Throwable error)
    {
        NavigationManager navigationManager = this.navigationManager.get();
        if (navigationManager != null)
        {
            navigationManager.goTo(new PlaceBuilder(ERROR_PLACE_NAME)
                    .parameter("error", error)
                    .build());
        }
        else
        {
            throw new UnhandledWorkerException("NavigationManager not set for error handler", error);
        }
    }
}
