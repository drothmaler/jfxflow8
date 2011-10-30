package com.zenjava.jfxflow.controller;

import com.zenjava.jfxflow.navigation.NavigationToolbar;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class BrowserSkin implements Skin
{
    private Browser browser;
    private StackPane root;
    private Node glassPane;
    private NavigationToolbar navigationToolbar;
    private Label titleLabel;
    private BorderPane rootPane;

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

        rootPane = new BorderPane();
        rootPane.setTop(buildHeader());
        rootPane.setCenter(browser.getControllerContainer());
        root.getChildren().add(rootPane);

        glassPane = buildGlassPane();
        glassPane.setVisible(false);
        root.getChildren().add(glassPane);

        glassPane.visibleProperty().bind(browser.busyProperty());
    }

    protected Node buildHeader()
    {
        StackPane header = new StackPane();
        header.getStyleClass().add("browser-header");

        titleLabel = new Label();
        titleLabel.textProperty().bind(browser.titleProperty());
        titleLabel.getStyleClass().add("browser-title");
        header.getChildren().add(titleLabel);

        navigationToolbar = new NavigationToolbar();
        navigationToolbar.homePlaceProperty().bind(browser.homePlaceProperty());
        navigationToolbar.navigationManagerProperty().bind(browser.navigationManagerProperty());
        header.getChildren().add(navigationToolbar);

        return header;
    }

    protected Node buildGlassPane()
    {
        BorderPane glassPane = new BorderPane();
        glassPane.getStyleClass().add("glass-pane");
        return glassPane;
    }
}
