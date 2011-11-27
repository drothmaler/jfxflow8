package com.zenjava.jfxflow.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ErrorPageSkin //implements Skin<ErrorPage>
{
    protected ErrorPage errorPage;
    protected VBox root;
    protected TextField messageField;
    protected TextArea stackTraceArea;

    public ErrorPageSkin(ErrorPage errorPage)
    {
        this.errorPage = errorPage;
        buildSkin();
    }

    public ErrorPage getSkinnable()
    {
        return errorPage;
    }

    public Node getNode()
    {
        return root;
    }

    public void dispose()
    {
    }

    protected void buildSkin()
    {
        root = new VBox(10);
        root.getStyleClass().add("error-page");

        Label headerLabel = new Label("Sorry, but not everything went to plan");
        headerLabel.getStyleClass().add("error-header");
        root.getChildren().add(headerLabel);

        Label infoLabel = new Label("An unexpected error has occurred while processing your request. The technical details of the error are below.");
        infoLabel.getStyleClass().add("error-info");
        root.getChildren().add(infoLabel);

        BorderPane detailPane = new BorderPane();
        detailPane.getStyleClass().add("error-detail");

        messageField = new TextField();
        messageField.getStyleClass().add("error-message");
        messageField.setEditable(false);
        detailPane.setTop(messageField);

        stackTraceArea = new TextArea();
        stackTraceArea.getStyleClass().add("error-stack-trace");
        stackTraceArea.setEditable(false);
        detailPane.setCenter(stackTraceArea);

        VBox.setVgrow(detailPane, Priority.ALWAYS);
        root.getChildren().add(detailPane);

        setError(errorPage.getError());
        errorPage.errorProperty().addListener(new ChangeListener<Throwable>()
        {
            public void changed(ObservableValue<? extends Throwable> source, Throwable oldValue, Throwable newValue)
            {
                setError(newValue);
            }
        });
    }

    protected void setError(Throwable error)
    {
        if (error != null)
        {
            messageField.setText(error.getMessage());
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            error.printStackTrace(printWriter);
            stackTraceArea.setText(writer.toString());
        }
        else
        {
            messageField.setText("");
            stackTraceArea.setText("");
        }
    }
}
