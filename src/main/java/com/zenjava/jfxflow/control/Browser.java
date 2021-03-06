package com.zenjava.jfxflow.control;

import com.sun.javafx.css.StyleManager;
import com.zenjava.jfxflow.actvity.ParentActivity;
import com.zenjava.jfxflow.actvity.ParentView;
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
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    private final ObjectProperty<NavigationManager> navigationManager = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> busyGlassPane = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> dialogGlassPane = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> header = new SimpleObjectProperty<>();
    private final ObjectProperty<Node> footer = new SimpleObjectProperty<>();
    private final ListProperty<Dialog> dialogs = new SimpleListProperty<>(FXCollections.observableArrayList());

    private ParentActivity<ParentView> contentPageActivity;

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

    @Override
    public void addDialog(Dialog dialog)
    {
        dialogs.add(dialog);
    }

    @Override
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

        this.navigationManager.addListener((source, oldNavigationManager, newNavigationManager) -> {
            contentPageActivity.currentPlaceProperty().unbind();
            if (newNavigationManager != null)
            {
                contentPageActivity.currentPlaceProperty().bind(newNavigationManager.currentPlaceProperty());
            }
        });

        this.header.set(new DefaultBrowserHeader(this, title));

        this.contentPageActivity = new ParentActivity<>(transitionFactory);
        SimpleParentView contentPageView = new SimpleParentView();
        contentPageView.getStyleClass().add("browser-content");
        this.contentPageActivity.setView(contentPageView);
        this.contentPageActivity.getPlaceResolvers().add(new RegexPlaceResolver(
                DefaultErrorHandler.ERROR_PLACE_NAME, new DefaultErrorActivity()));

        getChildren().add(buildView());

        // dialog glass pane

        this.dialogGlassPane.addListener((source, oldGlassPane, newGlassPane) -> {
            if (oldGlassPane != null)
            {
                getChildren().remove(oldGlassPane);
                oldGlassPane.visibleProperty().unbind();
            }
            if (newGlassPane != null)
            {
                newGlassPane.visibleProperty().bind(dialogs.emptyProperty().not());
                getChildren().add(newGlassPane);
            }
        });
        
        StackPane defaultGlassPane = new StackPane();
        defaultGlassPane.getStyleClass().add("dialog-glasspane");
        defaultGlassPane.setVisible(false);
        this.dialogGlassPane.set(defaultGlassPane);

        // busy glass pane

        this.busyGlassPane.addListener((source, oldGlassPane, newGlassPane) -> {
            if (oldGlassPane != null)
            {
                getChildren().remove(oldGlassPane);
                oldGlassPane.visibleProperty().unbind();
            }
            if (newGlassPane != null)
            {
                newGlassPane.visibleProperty().bind(
                        contentPageActivity.currentTransitionProperty().isNotNull().or(
                                contentPageActivity.workersProperty().emptyProperty().not()));
                getChildren().add(newGlassPane);
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
