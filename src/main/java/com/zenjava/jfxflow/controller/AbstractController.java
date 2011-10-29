/*
 * Copyright (c) 2011, Daniel Zwolenski. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController<PlaceType extends Place>
        implements Controller<PlaceType>, Initializable, HasFxmlLoadedView
{
    private Node view;
    private BooleanProperty busy;

    protected AbstractController()
    {
        busy = new SimpleBooleanProperty();
    }

    public void setView(Node view)
    {
        this.view = view;
    }

    public Node getView()
    {
        return view;
    }

    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public void activate(PlaceType place)
    {
    }

    public void deactivate()
    {
    }

    public BooleanProperty busyProperty()
    {
        return busy;
    }
}
