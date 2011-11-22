package com.zenjava.jfxflow.control;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class DefaultBrowserHeader extends StackPane
{
    protected Browser browser;
    protected BackButton backButton;
    protected ForwardButton forwardButton;
    protected Button refreshButton;
    protected StringProperty title;
    protected Pane leftDockBar;
    protected Pane titleBar;
    protected Pane rightDockBar;

    public DefaultBrowserHeader(Browser browser)
    {
        this(browser, null);
    }

    public DefaultBrowserHeader(Browser browser, String title)
    {
        this.browser = browser;
        getStyleClass().add("browser-header");
        this.title = new SimpleStringProperty(title);
        this.backButton = buildBackButton();
        this.forwardButton = buildForwardButton();
        this.refreshButton = buildRefreshButton();
        this.leftDockBar = buildLeftDockBar();
        this.titleBar = buildTitleBar();
        this.rightDockBar = buildRightDockBar();
        layoutHeader();
    }

    public Browser getBrowser()
    {
        return browser;
    }

    public BackButton getBackButton()
    {
        return backButton;
    }

    public ForwardButton getForwardButton()
    {
        return forwardButton;
    }

    public Button getRefreshButton()
    {
        return refreshButton;
    }

    public StringProperty titleProperty()
    {
        return title;
    }

    public String getTitle()
    {
        return title.get();
    }

    public void setTitle(String title)
    {
        this.title.set(title);
    }

    public Pane getLeftDockBar()
    {
        return leftDockBar;
    }

    public Pane getTitleBar()
    {
        return titleBar;
    }

    public Pane getRightDockBar()
    {
        return rightDockBar;
    }

    protected BackButton buildBackButton()
    {
        BackButton backButton = new BackButton("Back");
        backButton.navigationManagerProperty().bind(browser.navigationManagerProperty());
        return backButton;
    }

    protected ForwardButton buildForwardButton()
    {
        ForwardButton forwardButton = new ForwardButton("Forward");
        forwardButton.navigationManagerProperty().bind(browser.navigationManagerProperty());
        return forwardButton;
    }

    protected Button buildRefreshButton()
    {
        Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("refresh-button");
        refreshButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                browser.refresh();
            }
        });
        return refreshButton;
    }

    protected Pane buildLeftDockBar()
    {
        HBox leftDockBar = new HBox(10);
        leftDockBar.getChildren().addAll(getBackButton(), getForwardButton(), getRefreshButton());
        return leftDockBar;
    }

    protected Pane buildTitleBar()
    {
        BorderPane titleBar = new BorderPane();
        Label titleLabel = new Label();
        titleLabel.getStyleClass().add("title");
        titleLabel.textProperty().bind(titleProperty());
        titleBar.setCenter(titleLabel);
        return titleBar;
    }

    protected Pane buildRightDockBar()
    {
        return new HBox(10);
    }

    protected void layoutHeader()
    {
        Node titleBar = getTitleBar();
        StackPane.setAlignment(titleBar, Pos.CENTER);
        getChildren().add(titleBar);

        AnchorPane backLayer = new AnchorPane();

        Node left = getLeftDockBar();
        AnchorPane.setLeftAnchor(left, 0.0);
        backLayer.getChildren().add(left);

        Node right = getRightDockBar();
        AnchorPane.setRightAnchor(right, 0.0);
        backLayer.getChildren().add(right);

        getChildren().add(backLayer);
    }
}
