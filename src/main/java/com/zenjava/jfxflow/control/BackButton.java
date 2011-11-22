package com.zenjava.jfxflow.control;

import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.util.ListSizeBinding;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

// todo use Control + skin
public class BackButton extends NavigationButton
{
    private ObjectProperty<NavigationManager> navigationManager;

    public BackButton()
    {
        this(null, null, null);
    }

    public BackButton(NavigationManager navigationManager)
    {
        this(null, null, navigationManager);
    }

    public BackButton(String label)
    {
        this(label, null, null);
    }

    public BackButton(String label, NavigationManager navigationManager)
    {
        this(label, null, navigationManager);
    }

    public BackButton(Node graphic)
    {
        this(null, graphic, null);
    }

    public BackButton(Node graphic, NavigationManager navigationManager)
    {
        this(null, graphic, navigationManager);
    }

    public BackButton(String label, Node graphic)
    {
        this(label, graphic, null);
    }


    public BackButton(String label, Node graphic, final NavigationManager navigationManager)
    {
        super(label, graphic, navigationManager);
        getStyleClass().add("back-button");
    }

    protected void navigationManagerUpdated(NavigationManager oldNavigationManager, NavigationManager newNavigationManager)
    {
        disableProperty().unbind();
        if (newNavigationManager != null)
        {
            disableProperty().bind(new ListSizeBinding(newNavigationManager.getBackHistory(), 0));
        }
    }

    protected void doAction()
    {
        getNavigationManager().goBack();
    }
}
