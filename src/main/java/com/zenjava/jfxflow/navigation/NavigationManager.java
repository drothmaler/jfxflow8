package com.zenjava.jfxflow.navigation;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

/**
 * Provides a central point for controlling and monitoring navigation flow, including the current place and the back
 * and forward history lists. All aspects of the NavigationManager can be monitored for changes through property and
 * list listeners.
 * <p/>
 * An application can choose to have just one NavigationManger for the entire application (e.g. in the case of single
 * browser application), however for more advanced situations (such as a tabbed-browser, where each tab has its own
 * navigation history) any number of NavigationManagers can be used.
 */
public interface NavigationManager
{
    /**
     * Navigates to the specified place, making it the new current place. If a previous place was set then this is
     * added to the back history. If any forward history existed before this call then this will be discarded and the
     * forward list will be cleared.
     *
     * @param place the new place to navigate to.
     */
    void goTo(Place place);

    /**
     * Navigates back one place in the history. The last place in the back history will be removed from the back history
     * list and this will become the new current place. The previous current place (if any) will be moved into the first
     * position of the forward history list.
     * <p/>
     * Calling this method when the back history is empty will have no affect on either the current place or the back
     * history.
     */
    void goBack();

    /**
     * Navigates forward one place in the history. The first place in the forward history will be removed from the
     * forward history list and this will become the new current place. The previous current place (if any) will be
     * moved into the last  position of the back history list.
     * <p/>
     * Calling this method when the forward history is empty will have no affect on either the current place or the
     * forward history.
     */
    void goForward();


    /**
     * Retrieves the current place. This may be null if no current place has been set (i.e. when the system first
     * starts).
     *
     * @return the current place.
     */
    Place getCurrentPlace();

    /**
     * Sets the current place to the place specified, overwriting the existing current place. This method is provided
     * for advance control only. For general management of the current place and history it is recommended to use
     * the goTo, goBack and goForward methods instead.
     *
     * @param place the new current place.
     */
    void setCurrentPlace(Place place);

    /**
     * Retrieves the observable property for the current place. This can be used to monitor the current place with
     * a property change listener. This property is editable, however this is provided for advance control only. For
     * general management of the current place and history it is recommended to use the goTo, goBack and goForward
     * methods instead.
     *
     * @return the observable property for the current place of this navigation manager.
     */
    ObjectProperty<Place> currentPlaceProperty();

    /**
     * Retrieves the observable 'back' history list for this navigation manager. This contains all the places that have
     * previously been navigated to in order of least recent to most recent (i.e. the first place visited will be the
     * first place in the list - using goBack will step backwards through this list). This list is editable, however
     * this is provided for advance control only. For general management of the current place and history it is
     * recommended to use the goTo, goBack and goForward methods instead.
     *
     * @return the observable 'back' history list for this navigation manager.
     */
    ObservableList<Place> getBackHistory();

    /**
     * Retrieves the observable 'forward' history list for this navigation manager. This contains all the places that
     * have previously been navigated to and then backed out of. This list is in order of most recently backed out of
     * to least recent (i.e. the place just backed out of will be the first place in the list - using goForward will
     * step forwards through this list). This list is editable, however this is provided for advance control only. For
     * general management of the current place and history it is recommended to use the goTo, goBack and goForward
     * methods instead.
     *
     * @return the observable 'forward' history list for this navigation manager.
     */
    ObservableList<Place> getForwardHistory();
}
