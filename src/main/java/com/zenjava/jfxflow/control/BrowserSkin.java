package com.zenjava.jfxflow.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class BrowserSkin implements Skin<Browser>
{
    private Browser browser;
    private StackPane rootPane;
    private Node glassPane;
    private StackPane contentArea;
    private BackButton backButton;
    private ForwardButton forwardButton;

    public BrowserSkin(Browser browser)
    {
        this.browser = browser;
        buildSkin();
    }

    public Browser getSkinnable()
    {
        return browser;
    }

    public Node getNode()
    {
        return rootPane;
    }

    public void dispose()
    {
        backButton.navigationManagerProperty().unbind();
        forwardButton.navigationManagerProperty().unbind();
        browser.contentBoundsProperty().unbind();
    }

    protected void buildSkin()
    {
        rootPane = new StackPane();
        rootPane.getStyleClass().add("browser");

        BorderPane rootPaneLayout = new BorderPane();

        HBox navBar = new HBox(4);
        navBar.getStyleClass().add("toolbar");

        backButton = new BackButton("<");
        backButton.navigationManagerProperty().bind(browser.navigationManagerProperty());
        navBar.getChildren().add(backButton);

        forwardButton = new ForwardButton(">");
        forwardButton.navigationManagerProperty().bind(browser.navigationManagerProperty());
        navBar.getChildren().add(forwardButton);

        rootPaneLayout.setTop(navBar);

        this.contentArea = new StackPane();
        this.contentArea.getStyleClass().add("content");
        rootPaneLayout.setCenter(contentArea);

        rootPane.getChildren().add(rootPaneLayout);

        this.glassPane = new BorderPane();
        this.glassPane.setStyle("-fx-cursor: wait");
        this.glassPane.setVisible(false);
//        this.glassPane.visibleProperty().bind(animating.or(busy));
        rootPane.getChildren().add(this.glassPane);

        browser.contentBoundsProperty().bind(contentArea.boundsInParentProperty());

        Node currentContent = browser.contentProperty().get();
        if (currentContent != null)
        {
            contentArea.getChildren().add(currentContent);
        }

        browser.contentProperty().addListener(new ChangeListener<Node>()
        {
            public void changed(ObservableValue<? extends Node> source, Node oldNode, Node newNode)
            {
                if (oldNode != null)
                {
                    contentArea.getChildren().remove(oldNode);
                }
                if (newNode != null)
                {
                    contentArea.getChildren().add(newNode);
                }
            }
        });
    }
}
