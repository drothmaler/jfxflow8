package com.zenjava.jfxflow.control;

import com.sun.javafx.css.StyleManager;
import com.zenjava.jfxflow.actvity.*;
import com.zenjava.jfxflow.navigation.DefaultNavigationManager;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.PlaceResolver;
import com.zenjava.jfxflow.transition.*;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Control;

import java.lang.reflect.Field;

public class Browser extends Control implements HasNode, HasWorkers, IsRefreshable
{
    // work around for bug: http://javafx-jira.kenai.com/browse/RT-16647
    static
    {
        StyleManager.getInstance().addUserAgentStylesheet("styles/jfxflow-browser.css");
    }

    private ObjectProperty<NavigationManager> navigationManager;
    private ObjectProperty<HasNode> currentPage;
    private ObservableList<PlaceResolver> placeResolvers;
    private ObjectProperty<Animation> currentAnimation;
    private ObservableList<Worker> workers;
    private ObjectProperty<Node> content;
    private ObjectProperty<Bounds> contentBounds;
    private ObjectProperty<Node> header;
    private ObjectProperty<Node> footer;

    public Browser()
    {
        this(new DefaultNavigationManager());
    }

    public Browser(NavigationManager navigationManager)
    {
        getStyleClass().add("browser");

        this.navigationManager = new SimpleObjectProperty<NavigationManager>();
        this.currentPage = new SimpleObjectProperty<HasNode>();
        this.placeResolvers = FXCollections.observableArrayList();
        this.currentAnimation = new SimpleObjectProperty<Animation>();
        this.workers = FXCollections.observableArrayList();
        this.content = new SimpleObjectProperty<Node>();
        this.contentBounds = new SimpleObjectProperty<Bounds>();
        this.header = new SimpleObjectProperty<Node>();
        this.footer = new SimpleObjectProperty<Node>();

        this.header.set(new DefaultBrowserHeader(this));

        // manage current place

        final ChangeListener<? super Place> currentPlaceListener = new ChangeListener<Place>()
        {
            public void changed(ObservableValue<? extends Place> source, Place oldPlace, Place newPlace)
            {
                HasNode newPage = null;
                if (newPlace != null)
                {
                    for (PlaceResolver resolver : placeResolvers)
                    {
                        newPage = resolver.resolvePlace(newPlace);
                        if (newPage != null)
                        {
                            HasNode oldPage = currentPage.get();
                            if (oldPage instanceof Activatable)
                            {
                                ((Activatable) oldPage).setActive(false);
                            }

                            if (newPage instanceof Activatable)
                            {
                                setParameters(newPage, newPlace);
                                ((Activatable) newPage).setActive(true);
                            }
                            currentPage.set(newPage);
                            return;
                        }
                    }
                }
                currentPage.set(newPage);
            }
        };
        this.navigationManager.addListener(new ChangeListener<NavigationManager>()
        {
            public void changed(ObservableValue<? extends NavigationManager> source,
                                NavigationManager oldNavigationManager,
                                NavigationManager newNavigationManager)
            {
                if (oldNavigationManager != null)
                {
                    oldNavigationManager.currentPlaceProperty().removeListener(currentPlaceListener);
                }
                if (newNavigationManager != null)
                {
                    newNavigationManager.currentPlaceProperty().addListener(currentPlaceListener);
                }
            }
        });


        // manage current page

        final ListChangeListener<? super Worker> workerListListener = new ListChangeListener<Worker>()
        {
            public void onChanged(Change<? extends Worker> change)
            {
                // todo be more efficient about this
                workers.setAll(change.getList());
            }
        };

        this.currentPage.addListener(new ChangeListener<HasNode>()
        {
            public void changed(ObservableValue<? extends HasNode> source, HasNode oldPage, HasNode newPage)
            {
                workers.clear();

                if (oldPage instanceof HasWorkers)
                {
                    ((HasWorkers) oldPage).getWorkers().removeListener(workerListListener);
                }

                if (newPage instanceof HasWorkers)
                {
                    ((HasWorkers) newPage).getWorkers().addListener(workerListListener);
                }

                transition(oldPage, newPage);
            }
        });

        setNavigationManager(navigationManager);
    }

    public Node getNode()
    {
        return this;
    }

    public NavigationManager getNavigationManager()
    {
        return navigationManager.get();
    }

    public void setNavigationManager(NavigationManager navigationManager)
    {
        this.navigationManager.set(navigationManager);
    }

    public ObjectProperty<NavigationManager> navigationManagerProperty()
    {
        return navigationManager;
    }

    public ObservableList<PlaceResolver> getPlaceResolvers()
    {
        return placeResolvers;
    }

    public ObservableList<Worker> getWorkers()
    {
        return workers;
    }

    public ObjectProperty<Node> headerProperty()
    {
        return header;
    }

    public Node getHeader()
    {
        return header.get();
    }

    public void setHeader(Node header)
    {
        this.header.set(header);
    }

    public ObjectProperty<Node> footerProperty()
    {
        return footer;
    }

    public Node getFooter()
    {
        return footer.get();
    }

    public void setFooter(Node footer)
    {
        this.footer.set(footer);
    }

    public void refresh()
    {
        HasNode currentPage = this.currentPage.get();
        if (currentPage instanceof IsRefreshable)
        {
            ((IsRefreshable)currentPage).refresh();
        }
    }

    ObjectProperty<Animation> currentAnimationProperty()
    {
        return currentAnimation;
    }

    ObjectProperty<Node> contentProperty()
    {
        return content;
    }

    ObjectProperty<Bounds> contentBoundsProperty()
    {
        return contentBounds;
    }

    protected String getUserAgentStylesheet()
    {
        return "styles/jfxflow-browser.css";
    }

    protected void setParameters(HasNode page, Place place)
    {
        for (Field field : page.getClass().getDeclaredFields())
        {
            Param annotation = field.getAnnotation(Param.class);
            if (annotation != null)
            {
                String name = annotation.value();
                if (name == null || name.equals(""))
                {
                    name = field.getName();
                }

                Object value = place.getParameters().get(name);

                try
                {
                    field.setAccessible(true);
                    if (WritableValue.class.isAssignableFrom(field.getType()))
                    {
                        WritableValue property = (WritableValue) field.get(page);
                        property.setValue(value);
                    }
                    else
                    {
                        field.set(page, value);
                    }
                }
                catch (IllegalAccessException e)
                {
                    throw new ActivityParameterException(
                            String.format("Error setting property '%s' on field '%s' in Activity '%s'",
                                    name, field.getName(), page), e);
                }
            }
        }
    }

    protected void transition(final HasNode oldPage, HasNode newPage)
    {
        SequentialTransition transition = new SequentialTransition();

        ViewTransition exit = null;
        if (oldPage != null)
        {
            if (oldPage instanceof HasExitTransition)
            {
                exit = ((HasExitTransition) oldPage).getExitTransition();
            }
            else
            {
                exit = new FadeOutTransition(oldPage.getNode());
            }
            exit.setupBeforeAnimation(contentBounds.get());
            transition.getChildren().add(exit.getAnimation());
        }

        ViewTransition entry = null;
        if (newPage != null)
        {
            if (newPage instanceof HasEntryTransition)
            {
                entry = ((HasEntryTransition) newPage).getEntryTransition();
            }
            else
            {
                entry = new FadeInTransition(newPage.getNode());
            }
            entry.setupBeforeAnimation(contentBounds.get());
            transition.getChildren().add(entry.getAnimation());
            content.set(newPage.getNode());
        }

        final ViewTransition finalExit = exit;
        final ViewTransition finalEntry = entry;
        transition.setOnFinished(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                if (finalEntry != null)
                {
                    finalEntry.cleanupAfterAnimation();
                }
                if (finalExit != null)
                {
                    finalExit.cleanupAfterAnimation();
                }
            }
        });

        currentAnimation.set(transition);
        transition.play();

    }
}
