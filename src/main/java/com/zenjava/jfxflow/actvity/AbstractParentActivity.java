package com.zenjava.jfxflow.actvity;

import com.zenjava.jfxflow.util.ListBinding;
import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;

public abstract class AbstractParentActivity<ViewType extends View>
        extends AbstractActivity<ViewType> implements HasTransition
{
    private final ObjectProperty<Activity> currentActivity;
    private final ObjectProperty<Animation> currentTransition;
    private final ListBinding<Worker> workerListBinding;

    public AbstractParentActivity()
    {
        this.currentActivity = new SimpleObjectProperty<>();
        this.currentTransition = new SimpleObjectProperty<>();
        this.workerListBinding = new ListBinding<>(getWorkers());
    }

    public Activity getCurrentActivity()
    {
        return currentActivity.get();
    }

    public ReadOnlyObjectProperty<Activity> currentActivityProperty()
    {
        return this.currentActivity;
    }

    @Override
    public ReadOnlyObjectProperty<Animation> currentTransitionProperty()
    {
        return currentTransition;
    }

    public void showActivity(Activity newActivity, Animation transition)
    {
        workerListBinding.unbind();
        final Activity oldActivity = currentActivity.get();
        if (oldActivity instanceof Activatable)
        {
            ((Activatable) oldActivity).activeProperty().unbind();
        }

        this.currentActivity.set(newActivity);
        this.currentTransition.set(transition);

        if (newActivity instanceof HasWorkers)
        {
            workerListBinding.bind(((HasWorkers) newActivity).getWorkers());
        }
        if (newActivity instanceof Activatable)
        {
            ((Activatable) newActivity).activeProperty().bind(activeProperty());
        }

        if (transition != null)
        {
            final EventHandler<ActionEvent> originalOnFinished = transition.getOnFinished();
            transition.setOnFinished(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent event)
                {
                    if (originalOnFinished != null)
                    {
                        originalOnFinished.handle(event);
                        if (oldActivity instanceof Activatable)
                        {
                            ((Activatable) oldActivity).setActive(false);
                        }
                    }
                    currentTransition.set(null);
                }
            });
            transition.play();
        }
        else
        {
            if (oldActivity != null)
            {
                getContentArea().getChildren().remove(oldActivity.getView().toNode());
                if (oldActivity instanceof Activatable)
                {
                    ((Activatable) oldActivity).setActive(false);
                }
            }

            if (newActivity != null)
            {
                getContentArea().getChildren().add(newActivity.getView().toNode());
            }
        }
    }

    protected abstract StackPane getContentArea();
}
