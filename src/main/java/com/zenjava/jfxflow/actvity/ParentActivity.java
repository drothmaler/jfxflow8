package com.zenjava.jfxflow.actvity;

import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.PlaceResolver;
import com.zenjava.jfxflow.transition.DefaultTransitionFactory;
import com.zenjava.jfxflow.transition.TransitionFactory;
import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ParentActivity<ViewType extends ParentView>
        extends AbstractActivity<ViewType> implements HasTransition
{
    @Param("place") private final ObjectProperty<Place> currentPlace = new SimpleObjectProperty<>();
    private final ObservableList<PlaceResolver> placeResolvers = FXCollections.observableArrayList();
    private final ObjectProperty<Activity> currentActivity = new SimpleObjectProperty<>();
    private final ObjectProperty<TransitionFactory> transitionFactory;
    private final ObjectProperty<Animation> currentTransition = new SimpleObjectProperty<>();

    public ParentActivity()
    {
        this(null);
    }

    public ParentActivity(TransitionFactory transitionFactory)
    {
        this.transitionFactory = new SimpleObjectProperty<>(
                transitionFactory != null ? transitionFactory : new DefaultTransitionFactory());

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

    @Override
    public ReadOnlyObjectProperty<Animation> currentTransitionProperty()
    {
        return currentTransition;
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
        @Override
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
        private CurrentActivityListener() {
        }

        @Override
        public void changed(ObservableValue<? extends Activity> source, Activity oldActivity, Activity newActivity)
        {
            workersProperty().unbind();
            if (oldActivity instanceof Activatable)
            {
                ((Activatable) oldActivity).activeProperty().unbind();
                ((Activatable) oldActivity).setActive(false);
            }

            if (newActivity instanceof HasWorkers)
            {
                workersProperty().bind(((HasWorkers) newActivity).workersProperty());
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
                @Override
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
