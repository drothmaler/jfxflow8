package com.zenjava.demo.home;

import com.zenjava.demo.search.SearchPlace;
import com.zenjava.jfxflow.controller.AbstractController;
import com.zenjava.jfxflow.navigation.NavigationManager;
import javafx.event.ActionEvent;

public class HomeController extends AbstractController
{
    private NavigationManager navigationManager;

    public void setNavigationManager(NavigationManager navigationManager)
    {
        this.navigationManager = navigationManager;
    }

    public void search(ActionEvent event)
    {
        navigationManager.goTo(new SearchPlace());
    }

    public void add(ActionEvent event)
    {
    }
}
