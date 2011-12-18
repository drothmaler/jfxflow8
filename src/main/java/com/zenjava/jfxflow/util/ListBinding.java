package com.zenjava.jfxflow.util;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

// todo this can be replaced with list binding in JFX 2.1
public class ListBinding<E> implements ListChangeListener<E>
{
    private ObservableList<E> list;
    private ObservableList<E> boundTo;

    public ListBinding(ObservableList<E> list)
    {
        this.list = list;
    }

    public void onChanged(Change<? extends E> change)
    {
        list.setAll(boundTo);
    }

    public void bind(ObservableList<E> bindTo)
    {
        unbind();
        this.boundTo = bindTo;
        this.boundTo.addListener(this);
        list.setAll(boundTo);
    }

    public void unbind()
    {
        if (this.boundTo != null)
        {
            boundTo.removeListener(this);
        }
    }
}
