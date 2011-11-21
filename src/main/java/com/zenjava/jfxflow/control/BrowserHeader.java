package com.zenjava.jfxflow.control;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.List;

public class BrowserHeader extends StackPane
{
    private BackButton backButton;
    private ForwardButton forwardButton;

    public BrowserHeader(Browser browser)
    {
        this(browser, null);
    }

    public BrowserHeader(Browser browser, String title)
    {
        this(browser, title, null, null);
    }

    public BrowserHeader(Browser browser, String title,
                         List<? extends Node> additionalLeftNodes,
                         List<? extends Node> rightNodes)
    {
        HBox navBar = new HBox(4);
        navBar.getStyleClass().add("navigation-bar");

        backButton = new BackButton("<");
        backButton.navigationManagerProperty().bind(browser.navigationManagerProperty());
        navBar.getChildren().add(backButton);

        forwardButton = new ForwardButton(">");
        forwardButton.navigationManagerProperty().bind(browser.navigationManagerProperty());
        navBar.getChildren().add(forwardButton);

        if (additionalLeftNodes != null)
        {
            navBar.getChildren().addAll(additionalLeftNodes);
        }

        getChildren().add(navBar);

        Label titleLabel = null;
        if (title != null)
        {
            titleLabel = new Label(title);
            titleLabel.getStyleClass().add("title");
        }

        HBox right = null;
        if (rightNodes != null)
        {
            right = new HBox();
            right.getChildren().addAll(rightNodes);
        }

        createHeader(browser, navBar, titleLabel, right);
    }

    public BrowserHeader(Browser browser, Node left, Node center, Node right)
    {
        createHeader(browser, left, center, right);
    }

    protected void createHeader(Browser browser, Node left, Node center, Node right)
    {
        getStyleClass().add("toolbar");

        // back layer
        AnchorPane backLayer = new AnchorPane();

        if (left != null)
        {
            AnchorPane.setLeftAnchor(left, 0.0);
            backLayer.getChildren().add(left);
        }

        if (right != null)
        {
            AnchorPane.setRightAnchor(right, 0.0);
            backLayer.getChildren().add(right);
        }

        getChildren().add(backLayer);

        // front layer
        if (center != null)
        {
            StackPane.setAlignment(center, Pos.CENTER);
            getChildren().add(center);
        }
    }
}
