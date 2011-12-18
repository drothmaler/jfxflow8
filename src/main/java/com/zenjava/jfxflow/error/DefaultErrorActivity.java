package com.zenjava.jfxflow.error;

import com.sun.javafx.css.StyleManager;
import com.zenjava.jfxflow.actvity.Param;
import com.zenjava.jfxflow.actvity.SimpleActivity;
import com.zenjava.jfxflow.actvity.SimpleView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class DefaultErrorActivity extends SimpleActivity 
{
    static
    {
        StyleManager.getInstance().addUserAgentStylesheet(
                DefaultErrorActivity.class.getResource("/styles/jfxflow-error-page.css").toExternalForm());
    }

    @Param private ObjectProperty<Throwable> error;

    public DefaultErrorActivity()
    {
        this.error = new SimpleObjectProperty<Throwable>();

        VBox root = new VBox(10);
        root.getStyleClass().add("error-page");

        Label headerLabel = new Label("Sorry, but not everything went to plan");
        headerLabel.getStyleClass().add("error-header");
        root.getChildren().add(headerLabel);

        Label infoLabel = new Label("An unexpected error has occurred while processing your request. The technical details of the error are below.");
        infoLabel.getStyleClass().add("error-info");
        root.getChildren().add(infoLabel);

        BorderPane detailPane = new BorderPane();
        detailPane.getStyleClass().add("error-detail");

        final TextField messageField = new TextField();
        messageField.getStyleClass().add("error-message");
        messageField.setEditable(false);
        detailPane.setTop(messageField);

        final TextArea stackTraceArea = new TextArea();
        stackTraceArea.getStyleClass().add("error-stack-trace");
        stackTraceArea.setEditable(false);
        detailPane.setCenter(stackTraceArea);

        VBox.setVgrow(detailPane, Priority.ALWAYS);
        root.getChildren().add(detailPane);

        error.addListener(new ChangeListener<Throwable>()
        {
            public void changed(ObservableValue<? extends Throwable> source, Throwable oldValue, Throwable newValue)
            {
                if (error != null)
                {
                    messageField.setText(newValue.getMessage());
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    newValue.printStackTrace(printWriter);
                    stackTraceArea.setText(writer.toString());
                }
                else
                {
                    messageField.setText("");
                    stackTraceArea.setText("");
                }
            }
        });
        
        setView(new SimpleView(root));
    }
}
