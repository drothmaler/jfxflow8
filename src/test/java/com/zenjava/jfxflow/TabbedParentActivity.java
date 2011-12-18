package com.zenjava.jfxflow;

import com.zenjava.jfxflow.actvity.Activity;
import com.zenjava.jfxflow.actvity.ParentActivity;
import com.zenjava.jfxflow.actvity.SimpleActivity;
import com.zenjava.jfxflow.actvity.SimpleParentView;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.RegexPlaceResolver;
import com.zenjava.jfxflow.transition.DefaultTransitionFactory;
import com.zenjava.jfxflow.transition.FlyTransition;
import com.zenjava.jfxflow.transition.HorizontalPosition;
import com.zenjava.jfxflow.transition.ViewTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class TabbedParentActivity extends ParentActivity<SimpleParentView>
{
    public TabbedParentActivity()
    {
        setTransitionFactory(new SwipingTransitionFactory());
        getPlaceResolvers().add(new RegexPlaceResolver("tab1", createTab1Activity()));
        getPlaceResolvers().add(new RegexPlaceResolver("tab2", createTab2Activity()));

        SimpleParentView view = new SimpleParentView();
        VBox topArea = new VBox(10);
        topArea.setStyle("-fx-background-color: lightblue; -fx-padding: 20");
        topArea.getChildren().add(new Label("Tabbed Area"));

        HBox buttonBar = new HBox(10);

        Button tab1Button = new Button("Tab 1");
        tab1Button.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                setCurrentPlace(new Place("tab1"));
            }
        });
        buttonBar.getChildren().add(tab1Button);

        Button tab2Button = new Button("Tab 2");
        tab2Button.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                setCurrentPlace(new Place("tab2"));
            }
        });
        buttonBar.getChildren().add(tab2Button);

        Button badTab = new Button("Bad Tab");
        badTab.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                setCurrentPlace(new Place("badtab"));
            }
        });
        buttonBar.getChildren().add(badTab);

        topArea.getChildren().add(buttonBar);

        view.setTop(topArea);
        setView(view);
    }

    private Activity createTab1Activity()
    {
        BorderPane pane = new BorderPane();
        pane.setCenter(new Label("Tab 1"));
        pane.setStyle("-fx-border-color: gray; -fx-background-color: #ffd");
        return new SimpleActivity(pane);
    }

    private Activity createTab2Activity()
    {
        BorderPane pane = new BorderPane();
        pane.setCenter(new Label("Tab 2"));
        pane.setStyle("-fx-border-color: gray; -fx-background-color: #dff");
        return new SimpleActivity(pane);
    }

    //-------------------------------------------------------------------------

    private class SwipingTransitionFactory extends DefaultTransitionFactory
    {
        protected Transition createMainTransition(Activity fromActivity, Activity toActivity)
        {
            return new ParallelTransition();
        }

        protected ViewTransition getDefaultEntryTransition(Node node)
        {
            return FlyTransition.createFlyIn(node, Duration.millis(300), HorizontalPosition.right);
        }

        protected ViewTransition getDefaultExitTransition(Node node)
        {
            return FlyTransition.createFlyOut(node, Duration.millis(300), HorizontalPosition.left);
        }
    }
}
