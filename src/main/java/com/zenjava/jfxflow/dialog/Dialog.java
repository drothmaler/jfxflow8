package com.zenjava.jfxflow.dialog;

import com.sun.javafx.css.StyleManager;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Popup;
import javafx.stage.Window;

public class Dialog
{
    // needed since Popups are not skinnable in the normal way
    static
    {
        StyleManager.getInstance().addUserAgentStylesheet(
                Dialog.class.getResource("/styles/jfxflow-dialog.css").toExternalForm());
    }

    private ReadOnlyObjectWrapper<DialogOwner> owner;
    private Popup popup;
    private StringProperty title;
    private ObjectProperty<Node> content;
    private BorderPane contentArea;

    public Dialog()
    {
        this(null);
    }

    public Dialog(String title)
    {
        this.owner = new ReadOnlyObjectWrapper<DialogOwner>();
        this.popup = new Popup();
        this.title = new SimpleStringProperty(title);
        this.content = new SimpleObjectProperty<Node>();

        this.content.addListener(new ChangeListener<Node>()
        {
            public void changed(ObservableValue<? extends Node> source, Node oldNode, Node newNode)
            {
                contentArea.setCenter(newNode);
            }
        });

        this.popup.showingProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue)
            {
                DialogOwner owner = Dialog.this.owner.get();
                if (owner != null)
                {
                    if (newValue)
                    {
                        owner.addDialog(Dialog.this);
                    }
                    else
                    {
                        owner.removeDialog(Dialog.this);
                    }
                }
            }
        });

        buildSkin();
    }

    public ReadOnlyObjectProperty<DialogOwner> ownerProperty()
    {
        return owner;
    }

    public DialogOwner getOwner()
    {
        return owner.get();
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

    public ObjectProperty<Node> contentProperty()
    {
        return content;
    }

    public Node getContent()
    {
        return content.get();
    }

    public void setContent(Node content)
    {
        this.content.set(content);
    }

    public void show(Node node)
    {
        DialogOwner owner = null;
        Node nextNode = node;
        while (nextNode != null)
        {
            if (nextNode instanceof DialogOwner)
            {
                owner = (DialogOwner) nextNode;
            }
            nextNode = nextNode.getParent();
        }

        if (owner != null)
        {
            this.owner.set(owner);
            final Window window = node.getScene().getWindow();
            popup.show(window);
            popup.setX(window.getX() + (window.getWidth() / 2) - (popup.getWidth() / 2));
            popup.setY(window.getY() + (window.getHeight() / 2) - (popup.getHeight() / 2));
        }
        else
        {
            throw new NoDialogOwnerException(String.format(
                    "Node '%s' must have a parent that implements DialogOwner to be able to show a Dialog", node));
        }
    }

    public void hide()
    {
        popup.hide();
    }


    protected void buildSkin()
    {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dialog");

        BorderPane header = new BorderPane();
        header.getStyleClass().add("header");

        Label titleLabel = new Label(title.get());
        titleLabel.textProperty().bind(title);
        titleLabel.getStyleClass().add("title");
        header.setLeft(titleLabel);

        Button closeButton = new Button("Close");
        Label closeIcon = new Label();
        closeIcon.getStyleClass().add("close-icon");
        closeButton.setGraphic(closeIcon);
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                hide();
            }
        });
        header.setRight(closeButton);

        root.setTop(header);

        contentArea = new BorderPane();
        contentArea.getStyleClass().add("content");
        root.setCenter(contentArea);

        popup.getContent().add(root);
    }
}
