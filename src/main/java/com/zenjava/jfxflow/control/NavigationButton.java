package com.zenjava.jfxflow.control;

import com.zenjava.jfxflow.navigation.NavigationManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

public abstract class NavigationButton extends Button
{
    private ObjectProperty<NavigationManager> navigationManager;

    public NavigationButton()
    {
        this(null, null, null);
    }

    public NavigationButton(NavigationManager navigationManager)
    {
        this(null, null, navigationManager);
    }

    public NavigationButton(String label)
    {
        this(label, null, null);
    }

    public NavigationButton(String label, NavigationManager navigationManager)
    {
        this(label, null, navigationManager);
    }

    public NavigationButton(Node graphic)
    {
        this(null, graphic, null);
    }

    public NavigationButton(Node graphic, NavigationManager navigationManager)
    {
        this(null, graphic, navigationManager);
    }

    public NavigationButton(String label, Node graphic)
    {
        this(label, graphic, null);
    }

    public NavigationButton(String label, Node graphic, final NavigationManager navigationManager)
    {
        super(label, graphic);
        getStyleClass().add("navigation-button");

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
                NavigationManager manager = NavigationButton.this.navigationManager.get();
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
