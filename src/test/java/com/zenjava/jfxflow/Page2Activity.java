package com.zenjava.jfxflow;

import com.zenjava.jfxflow.actvity.AbstractActivity;
import com.zenjava.jfxflow.actvity.Param;
import com.zenjava.jfxflow.actvity.SimpleView;
import com.zenjava.jfxflow.control.PlaceHyperlink;
import com.zenjava.jfxflow.error.ErrorHandler;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.transition.*;
import com.zenjava.jfxflow.worker.BackgroundTask;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Page2Activity extends AbstractActivity<SimpleView<VBox>>
        implements HasEntryTransition, HasExitTransition
{
    @Param private String param1;

    private final Label messageLabel;
    private final ErrorHandler errorHandler;

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
        errorButton.setOnAction(event -> makeAnError());
        rootPane.getChildren().add(errorButton);

        setView(new SimpleView<>(rootPane));
    }

    @Override
    protected void activated()
    {
        messageLabel.setText("Loading...");
        executeTask(new BackgroundTask<String>(errorHandler)
        {
            @Override
            protected String call() throws Exception
            {
                Thread.sleep(1500);
                return "Fully loaded";
            }

            @Override
            protected void onSuccess(String message)
            {
                messageLabel.setText(message + " (param1 = " + param1 + ")");
            }
        });
    }

    protected void makeAnError()
    {
        executeTask(new BackgroundTask<String>(errorHandler)
        {
            @Override
            protected String call() throws Exception
            {
                throw new RuntimeException("A test error");
            }
        });
    }

    @Override
    public boolean isSequentialTransition()
    {
        return true;
    }

    @Override
    public ViewTransition getEntryTransition()
    {
        return FlyTransition.createFlyIn(getView().toNode(), Duration.millis(1000), VerticalPosition.top);
    }

    @Override
    public ViewTransition getExitTransition()
    {
        return FlyTransition.createFlyOut(getView().toNode(), Duration.millis(1000), VerticalPosition.top);
    }
}
