package com.zenjava.jfxflow.control;

import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.util.ListSizeBinding;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

// todo use Control + skin
public class BackHyperlink extends NavigationHyperlink
{
    private ObjectProperty<NavigationManager> navigationManager;

    public BackHyperlink()
    {
        this(null, null, null);
    }

    public BackHyperlink(NavigationManager navigationManager)
    {
        this(null, null, navigationManager);
    }

    public BackHyperlink(String label)
    {
        this(label, null, null);
    }

    public BackHyperlink(String label, NavigationManager navigationManager)
    {
        this(label, null, navigationManager);
    }

    public BackHyperlink(Node graphic)
    {
        this(null, graphic, null);
    }

    public BackHyperlink(Node graphic, NavigationManager navigationManager)
    {
        this(null, graphic, navigationManager);
    }

    public BackHyperlink(String label, Node graphic)
    {
        this(label, graphic, null);
    }


    public BackHyperlink(String label, Node graphic, final NavigationManager navigationManager)
    {
        super(label, graphic, navigationManager);
        getStyleClass().add("back-hyperlink");
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
