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
