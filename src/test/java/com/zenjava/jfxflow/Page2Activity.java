package com.zenjava.jfxflow;

import com.zenjava.jfxflow.actvity.AbstractActivity;
import com.zenjava.jfxflow.control.PlaceHyperlink;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.transition.*;
import com.zenjava.jfxflow.worker.BackgroundTask;
import com.zenjava.jfxflow.worker.ErrorHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Page2Activity extends AbstractActivity<VBox>
        implements HasEntryTransition, HasExitTransition
{
    private Label messageLabel;
    private ErrorHandler errorHandler;

    public Page2Activity(final NavigationManager navigationManager, ErrorHandler errorHandler)
    {
        this.errorHandler = errorHandler;

        VBox rootPane = new VBox(10);
        rootPane.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(new Label("Page 2"));
        rootPane.getChildren().add(new PlaceHyperlink(
                "Go to page 1", navigationManager, new Place("page1")));
        messageLabel = new Label();
        rootPane.getChildren().add(messageLabel);

        Button errorButton = new Button("Make an error");
        errorButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                makeAnError();
            }
        });
        rootPane.getChildren().add(errorButton);

        setNode(rootPane);
    }

    protected void activated()
    {
        messageLabel.setText("Loading...");
        executeTask(new BackgroundTask<String>(errorHandler)
        {
            protected String call() throws Exception
            {
                Thread.sleep(1500);
                return "Fully loaded";
            }

            protected void onSuccess(String message)
            {
                messageLabel.setText(message);
            }
        });
    }

    protected void makeAnError()
    {
        executeTask(new BackgroundTask<String>(errorHandler)
        {
            protected String call() throws Exception
            {
                throw new RuntimeException("A test error");
            }
        });
    }

    public ViewTransition getEntryTransition()
    {
        return FlyTransition.createFlyIn(getNode(), Duration.millis(1000), VerticalPosition.top);
    }

    public ViewTransition getExitTransition()
    {
        return FlyTransition.createFlyOut(getNode(), Duration.millis(1000), VerticalPosition.top);
    }
}
