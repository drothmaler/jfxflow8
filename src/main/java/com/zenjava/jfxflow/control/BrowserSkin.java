package com.zenjava.jfxflow.control;

import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.util.BooleanListBinding;
import com.zenjava.jfxflow.util.BooleanOperator;
import com.zenjava.jfxflow.util.ListSizeBinding;
import javafx.animation.Animation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BrowserSkin implements Skin<Browser>
{
    private Browser browser;
    private StackPane rootPane;
    private Node busyGlassPane;
    private Node dialogGlassPane;
    private Node invalidPlaceView;
    private StackPane contentArea;
    private BooleanProperty animationInProgress;
    private BooleanProperty workInProgress;
    private WorkerListener workerListener;

    public BrowserSkin(Browser browser)
    {
        this.browser = browser;
        buildSkin();
    }

    public Browser getSkinnable()
    {
        return browser;
    }

    public Node getNode()
    {
        return rootPane;
    }

    public void dispose()
    {
        browser.contentBoundsProperty().unbind();
        browser.getWorkers().removeListener(workerListener);
    }

    protected void buildSkin()
    {
        rootPane = new StackPane();
        rootPane.getStyleClass().add("browser");

        BorderPane rootPaneLayout = new BorderPane();

        this.contentArea = new StackPane();
        this.contentArea.getStyleClass().add("browser-content");
        rootPaneLayout.setCenter(contentArea);

        rootPaneLayout.topProperty().bind(browser.headerProperty());
        rootPaneLayout.bottomProperty().bind(browser.footerProperty());

        rootPane.getChildren().add(rootPaneLayout);

        this.dialogGlassPane = new BorderPane();
        this.dialogGlassPane.getStyleClass().add("dialog-glasspane");
        this.dialogGlassPane.setVisible(false);
        rootPane.getChildren().add(this.dialogGlassPane);

        this.busyGlassPane = new BorderPane();
        this.busyGlassPane.getStyleClass().add("busy-glasspane");
        this.busyGlassPane.setVisible(false);
        rootPane.getChildren().add(this.busyGlassPane);

        this.invalidPlaceView = buildInvalidPlacePage();

        browser.contentBoundsProperty().bind(contentArea.boundsInParentProperty());

        // watch dialogs and show dialog glass pane accordingly

        dialogGlassPane.visibleProperty().bind(new ListSizeBinding(browser.getDialogs(), 0).not());


        // watch active pages and update content area accordingly

        contentArea.getChildren().addAll(browser.getActivePages());

        browser.getActivePages().addListener(new ListChangeListener<Node>()
        {
            public void onChanged(Change<? extends Node> change)
            {
                while (change.next())
                {
                    if (change.wasPermutated())
                    {
                         for (int i = change.getFrom(); i < change.getTo(); ++i)
                         {
                            contentArea.getChildren().set(i, change.getList().get(i));
                         }
                    }
                    else
                    {
                        for (Node removed : change.getRemoved())
                        {
                            contentArea.getChildren().remove(removed);
                        }
                        for (Node added : change.getAddedSubList())
                        {
                            contentArea.getChildren().add(added);
                        }
                    }
                }
            }
        });

        browser.supportedPlaceProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    contentArea.getChildren().remove(invalidPlaceView);
                }
                else
                {
                    contentArea.getChildren().add(invalidPlaceView);
                }
            }
        });

        // watch busy state and update glasspane accordingly

        animationInProgress = new SimpleBooleanProperty();
        workInProgress = new SimpleBooleanProperty();
        busyGlassPane.visibleProperty().bind(animationInProgress.or(workInProgress));

        Animation currentAnimation = browser.currentAnimationProperty().get();
        if (currentAnimation != null)
        {
            animationInProgress.bind(currentAnimation.statusProperty().isEqualTo(Animation.Status.RUNNING));
        }

        browser.currentAnimationProperty().addListener(new ChangeListener<Animation>()
        {
            public void changed(ObservableValue<? extends Animation> source, Animation oldValue, Animation newValue)
            {
                animationInProgress.unbind();
                if (newValue != null)
                {
                    animationInProgress.bind(newValue.statusProperty().isEqualTo(Animation.Status.RUNNING));
                }
            }
        });

        workerListener = new WorkerListener();
        browser.getWorkers().addListener(workerListener);
    }

    private Node buildInvalidPlacePage()
    {
        final VBox box = new VBox(20);
        box.getStyleClass().add("invalid-place-page");

        final Label header = new Label("Sorry, that page does not exist");
        header.getStyleClass().add("invalid-place-header");
        box.getChildren().add(header);

        final Label message = new Label();
        browser.supportedPlaceProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue)
            {
                if (!newValue)
                {
                    Place place = null;
                    if (browser.getNavigationManager() != null)
                    {
                        place = browser.getNavigationManager().getCurrentPlace();
                    }
                    message.setText(place != null
                            ?  String.format("No page for '%s' could not be found", place.getName())
                            : "The page you are looking for could not be found.");
                }
            }
        });
        header.getStyleClass().add("invalid-place-message");
        box.getChildren().add(message);

        return box;
    }

    //-------------------------------------------------------------------------

    private class WorkerListener implements ListChangeListener<Worker>
    {
        private BooleanListBinding binding;

        private WorkerListener()
        {
            this.binding = new BooleanListBinding(BooleanOperator.or);
            workInProgress.bind(binding);
        }

        public void onChanged(Change<? extends Worker> change)
        {
            while (change.next())
            {
                if (!change.wasPermutated())
                {
                    for (Worker worker : change.getRemoved())
                    {
                        binding.getBooleanValues().remove(worker.runningProperty());
                    }
                    for (Worker worker : change.getAddedSubList())
                    {
                        binding.getBooleanValues().add(worker.runningProperty());
                    }
                }
            }
        }
    }
}
