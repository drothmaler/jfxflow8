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
package com.zenjava.jfxflow.controller;

import com.zenjava.jfxflow.navigation.Place;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;

public interface Controller<PlaceType extends Place>
{
    Node getView();

    void activate(PlaceType place);

    void deactivate();

    BooleanProperty busyProperty();
}
