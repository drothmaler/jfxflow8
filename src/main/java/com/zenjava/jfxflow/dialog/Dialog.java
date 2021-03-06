package com.zenjava.jfxflow.dialog;

import com.sun.javafx.css.StyleManager;
import javafx.beans.property.*;
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

    private final ReadOnlyObjectWrapper<DialogOwner> owner = new ReadOnlyObjectWrapper<>();
    private final Popup popup = new Popup();
    private final StringProperty title;
    private final ObjectProperty<Node> content = new SimpleObjectProperty<>();

    private BorderPane contentArea;

    public Dialog()
    {
        this(null);
    }

    public Dialog(String title)
    {
        this.title = new SimpleStringProperty(title);

        this.content.addListener((source, oldNode, newNode) -> contentArea.setCenter(newNode));

        this.popup.showingProperty().addListener((source, oldValue, newValue) -> {
            DialogOwner owner1 = Dialog.this.owner.get();
            if (owner1 != null)
            {
                if (newValue)
                {
                    owner1.addDialog(Dialog.this);
                }
                else
                {
                    owner1.removeDialog(Dialog.this);
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
        closeButton.setOnAction(event -> hide());
        header.setRight(closeButton);

        root.setTop(header);

        contentArea = new BorderPane();
        contentArea.getStyleClass().add("content");
        root.setCenter(contentArea);

        popup.getContent().add(root);
    }
}
