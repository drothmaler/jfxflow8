package com.zenjava.jfxflow;

import com.zenjava.jfxflow.actvity.AbstractActivity;
import com.zenjava.jfxflow.actvity.SimpleView;
import com.zenjava.jfxflow.control.PlaceButton;
import com.zenjava.jfxflow.dialog.Dialog;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.PlaceBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Page1Activity extends AbstractActivity<SimpleView<VBox>>
{
    public Page1Activity(final NavigationManager navigationManager)
    {
        VBox rootPane = new VBox(10);
        rootPane.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(new Label("Page 1"));
        rootPane.getChildren().add(new PlaceButton(
                "Go to page 2", navigationManager,
                new PlaceBuilder("page2").parameter("param1", "TEST").build()));
        rootPane.getChildren().add(new PlaceButton(
                "Go to tabbed parent view", navigationManager, new Place("tabbed")));

        rootPane.getChildren().add(new PlaceButton(
                "Go to a bad place", navigationManager, new Place("badplace")));

        final Button showDialogButton = new Button("Show Dialog");
        showDialogButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                Dialog dialog = new Dialog("Test Dialog");
                dialog.setContent(new Label("This is a test dialog"));
                dialog.show(showDialogButton);
            }
        });
        rootPane.getChildren().add(showDialogButton);

        setView(new SimpleView<VBox>(rootPane));
    }
}
