package com.zenjava.jfxflow.control;

import com.zenjava.jfxflow.util.BooleanListBinding;
import com.zenjava.jfxflow.util.BooleanOperator;
import javafx.animation.Animation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class BrowserSkin implements Skin<Browser>
{
    private Browser browser;
    private StackPane rootPane;
    private Node glassPane;
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

        rootPaneLayout.topProperty().bind(browser.headerProperty());
        rootPaneLayout.bottomProperty().bind(browser.footerProperty());

        this.contentArea = new StackPane();
        this.contentArea.getStyleClass().add("content");
        rootPaneLayout.setCenter(contentArea);

        rootPane.getChildren().add(rootPaneLayout);

        this.glassPane = new BorderPane();
        this.glassPane.getStyleClass().add("glasspane");
        this.glassPane.setVisible(false);
        rootPane.getChildren().add(this.glassPane);

        browser.contentBoundsProperty().bind(contentArea.boundsInParentProperty());

        // watch current content and update content area accordingly

        Node currentContent = browser.contentProperty().get();
        if (currentContent != null)
        {
            contentArea.getChildren().add(currentContent);
        }

        browser.contentProperty().addListener(new ChangeListener<Node>()
        {
            public void changed(ObservableValue<? extends Node> source, Node oldNode, Node newNode)
            {
                if (oldNode != null)
                {
                    contentArea.getChildren().remove(oldNode);
                }
                if (newNode != null)
                {
                    contentArea.getChildren().add(newNode);
                }
            }
        });

        // watch busy state and update glasspane accordingly

        animationInProgress = new SimpleBooleanProperty();
        workInProgress = new SimpleBooleanProperty();
        glassPane.visibleProperty().bind(animationInProgress.or(workInProgress));

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
