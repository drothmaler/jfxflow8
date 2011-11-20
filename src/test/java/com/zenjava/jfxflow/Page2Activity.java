package com.zenjava.jfxflow;

import com.zenjava.jfxflow.actvity.AbstractActivity;
import com.zenjava.jfxflow.control.PlaceHyperlink;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.navigation.Place;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Page2Activity extends AbstractActivity<VBox>
{
    public Page2Activity(final NavigationManager navigationManager)
    {
        VBox rootPane = new VBox(10);
        rootPane.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(new Label("Page 2"));
        rootPane.getChildren().add(new PlaceHyperlink(
                "Go to page 1", navigationManager, new Place("page1")));
        setNode(rootPane);
    }
}
