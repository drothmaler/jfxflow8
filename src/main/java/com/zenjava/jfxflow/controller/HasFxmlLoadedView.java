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

import javafx.scene.Node;

/**
 * This is a little hack interface to allow us to set the view in the FxmlControllerLoader. Ideally we would have be
 * able to have our FXML automatically inject the view into the controller, but when the view is specified in a base
 * class (as in AbstractController), FXML does not detect it. This interface allows us to brute-force inject the view
 * after it has been loaded in hte FxmlControllerLoader. Use of this interface for any other reason is a bad move.
 */
public interface HasFxmlLoadedView
{
    public void setView(Node view);
}
