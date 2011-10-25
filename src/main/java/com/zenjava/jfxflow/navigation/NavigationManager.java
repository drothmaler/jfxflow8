package com.zenjava.jfxflow.navigation;

import java.util.List;

public interface NavigationManager
{
    void goTo(Place place);

    void refresh();

    void goBack();

    void goForward();


    List<Place> getHistory();

    int getCurrentPlaceInHistory();

    Place getCurrentPlace();


    void addNavigationListener(NavigationListener listener);

    void removeNavigationListener(NavigationListener listener);
}
