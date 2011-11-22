package com.zenjava.jfxflow.control;

import com.zenjava.jfxflow.navigation.NavigationManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;

public abstract class NavigationHyperlink extends Hyperlink
{
    private ObjectProperty<NavigationManager> navigationManager;

    public NavigationHyperlink()
    {
        this(null, null, null);
    }

    public NavigationHyperlink(NavigationManager navigationManager)
    {
        this(null, null, navigationManager);
    }

    public NavigationHyperlink(String label)
    {
        this(label, null, null);
    }

    public NavigationHyperlink(String label, NavigationManager navigationManager)
    {
        this(label, null, navigationManager);
    }

    public NavigationHyperlink(Node graphic)
    {
        this(null, graphic, null);
    }

    public NavigationHyperlink(Node graphic, NavigationManager navigationManager)
    {
        this(null, graphic, navigationManager);
    }

    public NavigationHyperlink(String label, Node graphic)
    {
        this(label, graphic, null);
    }

    public NavigationHyperlink(String label, Node graphic, final NavigationManager navigationManager)
    {
        super(label, graphic);
        getStyleClass().add("navigation-hyperlink");

        this.navigationManager = new SimpleObjectProperty<NavigationManager>();
        this.navigationManager.addListener(new ChangeListener<NavigationManager>()
        {
            public void changed(ObservableValue<? extends NavigationManager> source,
                                NavigationManager oldNavigationManager,
                                NavigationManager newNavigationManager)
            {
                navigationManagerUpdated(oldNavigationManager, newNavigationManager);
            }
        });

        setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                NavigationManager manager = NavigationHyperlink.this.navigationManager.get();
                if (manager != null)
                {
                    doAction();
                }
                else
                {
                    throw new IllegalStateException("A NavigationManager must be set before using this NavigationButton");
                }
            }
        });

        this.navigationManager.set(navigationManager);
    }

    protected void navigationManagerUpdated(NavigationManager oldNavigationManager, NavigationManager newNavigationManager)
    {

    }

    protected abstract void doAction();

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
}
