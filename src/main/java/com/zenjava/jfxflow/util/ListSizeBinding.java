package com.zenjava.jfxflow.util;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;

/**
 * Boolean binding expression for the size of a list. If the list bound to has the size specified then this binding will
 * evaluate to true, otherwise it will evaluate to false.
 *
 * todo confirm there is nothing for this already in JavaFX?
 */
public class ListSizeBinding extends BooleanBinding
{
    private ObservableList list;
    private int size;

    /**
     * Constructs a binding on the size of the specified list, comparing it to the size specified. When the size of the
     * list matches the target size specified, this expression will evaluate to true.
     *
     * @param list the list to bind to.
     * @param size the target size for the expression.
     */
    public ListSizeBinding(ObservableList list, int size)
    {
        this.list = list;
        this.size = size;
        bind(list);
    }

    /**
     * Compares the current size of the bound-to list with the target size. If the sizes are equal then this method
     * returns true, otherwise it returns false.
     *
     * @return true if the current size of the bound-to list equals the target size for this binding, false otherwise.
     */
    protected boolean computeValue()
    {
        return list.size() == size;
    }
}