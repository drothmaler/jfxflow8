/*
 * Copyright (c) 2011, Daniel Zwolenski. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package com.zenjava.jfxflow.navigation;

import java.util.List;

public interface NavigationManager
{
    /**
      * Navigates to the place specified by appending the place to the history immediately after the current place. This
      * is identical to calling goTo(place, false).
      *
      * @param place the place to navigate to.
      */
     void goTo(Place place);

     /**
      * Navigates to the place specified. If the replaceCurrent parameter is true then the new place will overwrite the
      * current place in the history (removing the previous current place from the history all together). If the value is
      * set to false then the new place will be appended to the history, immediately after the current place (leaving the
      * previous current place in the history). In either case, the new place will become the new current place.
      *
      * If the current place is not the last place in the history when this method is called (i.e. the goBack() method
      * has been called) then any forward history after the current place is discarded.
      *
      * @param place the new place to navigate to. This will become the current place.
      * @param overwriteCurrent true if the specified place should replace the current place in the history, false if
      * the new place should just be appended to the history.
      */
     void goTo(Place place, boolean overwriteCurrent);

    void refresh();

    void goBack();

    void goForward();


    List<Place> getHistory();

    int getCurrentPlaceInHistory();

    Place getCurrentPlace();


    void addNavigationListener(NavigationListener listener);

    void removeNavigationListener(NavigationListener listener);
}
