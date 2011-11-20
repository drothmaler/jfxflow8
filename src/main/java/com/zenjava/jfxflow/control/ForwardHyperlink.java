package com.zenjava.jfxflow.control;

import com.zenjava.jfxflow.navigation.NavigationManager;
import javafx.scene.Node;

// todo use Control + skin
public class ForwardHyperlink extends NavigationHyperlink
{
    public ForwardHyperlink()
    {
        this(null, null, null);
    }

    public ForwardHyperlink(NavigationManager navigationManager)
    {
        this(null, null, navigationManager);
    }

    public ForwardHyperlink(String label)
    {
        this(label, null, null);
    }

    public ForwardHyperlink(String label, NavigationManager navigationManager)
    {
        this(label, null, navigationManager);
    }

    public ForwardHyperlink(Node graphic)
    {
        this(null, graphic, null);
    }

    public ForwardHyperlink(Node graphic, NavigationManager navigationManager)
    {
        this(null, graphic, navigationManager);
    }

    public ForwardHyperlink(String label, Node graphic)
    {
        this(label, graphic, null);
    }

    public ForwardHyperlink(String label, Node graphic, final NavigationManager navigationManager)
    {
        super(label, graphic, navigationManager);
    }

    protected void navigationManagerUpdated(NavigationManager oldNavigationManager, NavigationManager newNavigationManager)
    {
        disableProperty().unbind();
        if (newNavigationManager != null)
        {
            disableProperty().bind(new ListSizeBinding(newNavigationManager.getForwardHistory(), 0));
        }
    }

    protected void doAction()
    {
        getNavigationManager().goForward();
    }
}
