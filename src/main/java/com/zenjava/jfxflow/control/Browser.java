package com.zenjava.jfxflow.control;

import com.sun.javafx.css.StyleManager;
import com.zenjava.jfxflow.actvity.ParentActivity;
import com.zenjava.jfxflow.actvity.SimpleParentView;
import com.zenjava.jfxflow.dialog.Dialog;
import com.zenjava.jfxflow.dialog.DialogOwner;
import com.zenjava.jfxflow.error.DefaultErrorActivity;
import com.zenjava.jfxflow.error.DefaultErrorHandler;
import com.zenjava.jfxflow.navigation.DefaultNavigationManager;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.PlaceResolver;
import com.zenjava.jfxflow.navigation.RegexPlaceResolver;
import com.zenjava.jfxflow.transition.TransitionFactory;
import com.zenjava.jfxflow.util.ListSizeBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class Browser extends StackPane implements DialogOwner
{
    // work around for bug: http://javafx-jira.kenai.com/browse/RT-16647
    static
    {
        StyleManager.getInstance().addUserAgentStylesheet(
                Browser.class.getResource("/styles/jfxflow-browser.css").toExternalForm());
    }

    private ObjectProperty<NavigationManager> navigationManager;
    private ParentActivity contentPageActivity;
    private ObjectProperty<Node> busyGlassPane;
    private ObjectProperty<Node> dialogGlassPane;
    private ObjectProperty<Node> header;
    private ObjectProperty<Node> footer;
    private ObservableList<Dialog> dialogs;

    public Browser()
    {
        this(new DefaultNavigationManager());
    }

    public Browser(NavigationManager navigationManager)
    {
        this(navigationManager, null, null);
    }

    public Browser(String title)
    {
        this(new DefaultNavigationManager(), null, title);
    }

    public Browser(NavigationManager navigationManager, String title)
    {
        this(navigationManager, null, title);
    }

    public Browser(NavigationManager navigationManager, TransitionFactory transitionFactory, String title)
    {
        buildView(navigationManager, transitionFactory, title);
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

    public ParentActivity getContentPageActivity()
    {
        return contentPageActivity;
    }

    public ObservableList<PlaceResolver> getPlaceResolvers()
    {
        return contentPageActivity.getPlaceResolvers();
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
        contentPageActivity.setActive(false);
        contentPageActivity.setActive(true);
    }

    public void addDialog(Dialog dialog)
    {
        dialogs.add(dialog);
    }

    public void removeDialog(Dialog dialog)
    {
        dialogs.remove(dialog);
    }

    ObservableList<Dialog> getDialogs()
    {
        return dialogs;
    }

    protected String getUserAgentStylesheet()
    {
        return Browser.class.getResource("jfxflow-browser.css").toExternalForm();
    }

    private void buildView(NavigationManager navigationManager, TransitionFactory transitionFactory, String title)
    {
        getStyleClass().add("browser");

        this.navigationManager = new SimpleObjectProperty<NavigationManager>();
        this.navigationManager.addListener(new ChangeListener<NavigationManager>()
        {
            public void changed(ObservableValue<? extends NavigationManager> source,
                                NavigationManager oldNavigationManager,
                                NavigationManager newNavigationManager)
            {
                contentPageActivity.currentPlaceProperty().unbind();
                if (newNavigationManager != null)
                {
                    contentPageActivity.currentPlaceProperty().bind(newNavigationManager.currentPlaceProperty());
                }
            }
        });

        this.header = new SimpleObjectProperty<Node>();
        this.header.set(new DefaultBrowserHeader(this, title));

        this.contentPageActivity = new ParentActivity(transitionFactory);
        SimpleParentView contentPageView = new SimpleParentView();
        contentPageView.getStyleClass().add("browser-content");
        this.contentPageActivity.setView(contentPageView);
        this.contentPageActivity.getPlaceResolvers().add(new RegexPlaceResolver(
                DefaultErrorHandler.ERROR_PLACE_NAME, new DefaultErrorActivity()));

        this.footer = new SimpleObjectProperty<Node>();

        getChildren().add(buildView());

        // dialog glass pane

        this.dialogs = FXCollections.observableArrayList();
        this.dialogGlassPane = new SimpleObjectProperty<Node>();
        this.dialogGlassPane.addListener(new ChangeListener<Node>()
        {
            public void changed(ObservableValue<? extends Node> source, Node oldGlassPane, Node newGlassPane)
            {
                if (oldGlassPane != null)
                {
                    getChildren().remove(oldGlassPane);
                    oldGlassPane.visibleProperty().unbind();
                }
                if (newGlassPane != null)
                {
                    newGlassPane.visibleProperty().bind(new ListSizeBinding(dialogs, 0).not());
                    getChildren().add(newGlassPane);
                }
            }
        });
        
        StackPane defaultGlassPane = new StackPane();
        defaultGlassPane.getStyleClass().add("dialog-glasspane");
        defaultGlassPane.setVisible(false);
        this.dialogGlassPane.set(defaultGlassPane);

        // busy glass pane

        this.busyGlassPane = new SimpleObjectProperty<Node>();
        this.busyGlassPane.addListener(new ChangeListener<Node>()
        {
            public void changed(ObservableValue<? extends Node> source, Node oldGlassPane, Node newGlassPane)
            {
                if (oldGlassPane != null)
                {
                    getChildren().remove(oldGlassPane);
                    oldGlassPane.visibleProperty().unbind();
                }
                if (newGlassPane != null)
                {
                    newGlassPane.visibleProperty().bind(
                            contentPageActivity.currentTransitionProperty().isNotNull().or(
                                    new ListSizeBinding(contentPageActivity.getWorkers(), 0).not()));
                    getChildren().add(newGlassPane);
                }
            }
        });
        StackPane defaultBusyGlassPane = new StackPane();
        defaultBusyGlassPane.getStyleClass().add("busy-glasspane");
        defaultBusyGlassPane.setVisible(false);
        this.busyGlassPane.set(defaultBusyGlassPane);

        setNavigationManager(navigationManager);
        contentPageActivity.setActive(true);
    }

    protected Node buildView()
    {
        BorderPane rootPaneLayout = new BorderPane();
        rootPaneLayout.topProperty().bind(header);
        rootPaneLayout.setCenter(contentPageActivity.getView().toNode());
        rootPaneLayout.bottomProperty().bind(footer);
        return rootPaneLayout;
    }
}
