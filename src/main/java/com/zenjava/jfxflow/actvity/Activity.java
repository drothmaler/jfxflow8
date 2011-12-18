package com.zenjava.jfxflow.actvity;

public interface Activity<ViewType extends View>
{
    ViewType getView();
}
