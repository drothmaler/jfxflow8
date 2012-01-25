package com.zenjava.jfxflow.actvity;

import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.PlaceResolver;
import com.zenjava.jfxflow.transition.DefaultTransitionFactory;
import com.zenjava.jfxflow.transition.TransitionFactory;
import com.zenjava.jfxflow.util.ListBinding;
import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ParentActivity<ViewType extends ParentView>
        extends AbstractActivity<ViewType>
{
    @Param("place") private ObjectProperty<Place> currentPlace;
    private ObservableList<PlaceResolver> placeResolvers;
    private ObjectProperty<Activity> currentActivity;
    private ObjectProperty<TransitionFactory> transitionFactory;
    private ObjectProperty<Animation> currentTransition;

    public ParentActivity()
    {
        this(null);
    }

    public ParentActivity(TransitionFactory transitionFactory)
    {
        this.currentPlace = new SimpleObjectProperty<Place>();
        this.placeResolvers = FXCollections.observableArrayList();
        this.currentActivity = new SimpleObjectProperty<Activity>();
        this.transitionFactory = new SimpleObjectProperty<TransitionFactory>(
                transitionFactory != null ? transitionFactory : new DefaultTransitionFactory());
        this.currentTransition = new SimpleObjectProperty<Animation>();

        currentPlace.addListener(new CurrentPlaceListener());
        currentActivity.addListener(new CurrentActivityListener());
    }

    public ObjectProperty<Place> currentPlaceProperty()
    {
        return currentPlace;
    }

    public Place getCurrentPlace()
    {
        return currentPlace.get();
    }

    public void setCurrentPlace(Place currentPlace)
    {
        this.currentPlace.set(currentPlace);
    }

    public ObservableList<PlaceResolver> getPlaceResolvers()
    {
        return placeResolvers;
    }

    public ObjectProperty<TransitionFactory> transitionFactoryProperty()
    {
        return transitionFactory;
    }

    public TransitionFactory getTransitionFactory()
    {
        return transitionFactory.get();
    }

    public void setTransitionFactory(TransitionFactory transitionFactory)
    {
        this.transitionFactory.set(transitionFactory);
    }

    public ReadOnlyObjectProperty<Animation> currentTransitionProperty()
    {
        return currentTransition;
    }

    public Animation getCurrentTransition()
    {
        return currentTransition.get();
    }

    protected Activity createUnsupportedPlaceActivity(Place place)
    {
        final VBox box = new VBox(20);
        box.getStyleClass().add("invalid-place-page");

        final Label header = new Label("Sorry, that page does not exist");
        header.getStyleClass().add("invalid-place-header");
        box.getChildren().add(header);

        final Label message = new Label(place != null
                ?  String.format("The page for '%s' could not be found", place.getName())
                : "The page you are looking for could not be found.");
        message.getStyleClass().add("invalid-place-message");
        box.getChildren().add(message);

        return new SimpleActivity(box);
    }

    //-------------------------------------------------------------------------

    private class CurrentPlaceListener implements ChangeListener<Place>
    {
        public void changed(ObservableValue<? extends Place> source, Place oldPlace, Place newPlace)
        {
            if (newPlace != null)
            {
                for (int i = placeResolvers.size() - 1; i >= 0; i--)
                {
                    PlaceResolver resolver = placeResolvers.get(i);
                    Activity newActivity = resolver.resolvePlace(newPlace);
                    if (newActivity != null)
                    {
                        currentActivity.set(newActivity);
                        return;
                    }
                }
            }

            // no matching place
            currentActivity.set(createUnsupportedPlaceActivity(newPlace));
        }
    }

    //-------------------------------------------------------------------------

    private class CurrentActivityListener implements ChangeListener<Activity>
    {
        private ListBinding<Worker> workerListBinding;

        private CurrentActivityListener()
        {
            this.workerListBinding = new ListBinding<Worker>(getWorkers());
        }

        public void changed(ObservableValue<? extends Activity> source, Activity oldActivity, Activity newActivity)
        {
            workerListBinding.unbind();
            if (oldActivity instanceof Activatable)
            {
                ((Activatable) oldActivity).activeProperty().unbind();
                ((Activatable) oldActivity).setActive(false);
            }

            if (newActivity instanceof HasWorkers)
            {
                workerListBinding.bind(((HasWorkers) newActivity).getWorkers());
            }
            if (newActivity instanceof Activatable)
            {
                ((Activatable) newActivity).activeProperty().bind(activeProperty());
            }

            currentTransition.unbind();
            Animation transition = transitionFactory.get().createTransition(
                    getView().getChildArea(), oldActivity, newActivity);
            currentTransition.set(transition);
            final EventHandler<ActionEvent> originalOnFinished = transition.getOnFinished();
            transition.setOnFinished(new EventHandler<ActionEvent>()
            {
                public void handle(ActionEvent event)
                {
                    if (originalOnFinished != null)
                    {
                        originalOnFinished.handle(event);
                    }

                    if (currentActivity.get() instanceof HasTransition)
                    {
                        currentTransition.bind(((HasTransition) currentActivity.get()).currentTransitionProperty());
                    }
                    else
                    {
                        currentTransition.set(null);
                    }
                }
            });
            transition.play();


        }
    }
}
