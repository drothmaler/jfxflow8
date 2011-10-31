package com.zenjava.jfxflow.controller;

import com.zenjava.jfxflow.navigation.NavigationToolbar;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class BrowserSkin implements Skin
{
    private Browser browser;
    private StackPane root;
    private Node glassPane;
    private NavigationToolbar navigationToolbar;
    private Label titleLabel;
    private HBox dockBar;

    public BrowserSkin(Browser browser)
    {
        this.browser = browser;
        buildView();
    }

    public Skinnable getSkinnable()
    {
        return browser;
    }

    public Node getNode()
    {
        return root;
    }

    public void dispose()
    {
        titleLabel.textProperty().unbind();
        glassPane.visibleProperty().unbind();
    }

    protected void buildView()
    {
        root = new StackPane();
        root.getStyleClass().add("browser");

        BorderPane rootPane = new BorderPane();
        rootPane.setTop(buildHeader());
        rootPane.setCenter(browser.getControllerContainer());
        root.getChildren().add(rootPane);

        glassPane = buildGlassPane();
        glassPane.setVisible(false);
        root.getChildren().add(glassPane);

        glassPane.visibleProperty().bind(browser.busyProperty());

//        browser.getDockBarItems().addListener(new ListChangeListener<Node>()
//        {
//            public void onChanged(Change<? extends Node> change)
//            {
//                dockBar.getChildren().removeAll(change.getRemoved());
//                dockBar.getChildren().addAll(change.getRemoved());
//            }
//        });
    }

    protected Node buildHeader()
    {
        StackPane header = new StackPane();
        header.getStyleClass().add("browser-header");

        AnchorPane dockPane = new AnchorPane();

        navigationToolbar = new NavigationToolbar();
        navigationToolbar.homePlaceProperty().bind(browser.homePlaceProperty());
        navigationToolbar.navigationManagerProperty().bind(browser.navigationManagerProperty());
        AnchorPane.setLeftAnchor(navigationToolbar, 0.0);
        dockPane.getChildren().add(navigationToolbar);

        dockBar = new HBox();
        AnchorPane.setRightAnchor(dockPane, 0.0);
        dockPane.getChildren().add(dockBar);

        header.getChildren().add(dockPane);

        titleLabel = new Label();
        titleLabel.textProperty().bind(browser.titleProperty());
        titleLabel.getStyleClass().add("browser-title");
        header.getChildren().add(titleLabel);

        return header;
    }

    protected Node buildGlassPane()
    {
        BorderPane glassPane = new BorderPane();
        glassPane.getStyleClass().add("glass-pane");
        return glassPane;
    }
}
