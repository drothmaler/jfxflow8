package com.zenjava.jfxflow;

import com.zenjava.jfxflow.actvity.AbstractActivity;
import com.zenjava.jfxflow.control.PlaceButton;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Page1Activity extends AbstractActivity<VBox>
{
    public Page1Activity(final NavigationManager navigationManager)
    {
        VBox rootPane = new VBox(10);
        rootPane.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(new Label("Page 1"));
        rootPane.getChildren().add(new PlaceButton(
                "Go to page 2", navigationManager, new Place("page2")));
        setNode(rootPane);
    }
}
