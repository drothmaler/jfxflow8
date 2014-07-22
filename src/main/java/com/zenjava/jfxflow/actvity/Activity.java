package com.zenjava.jfxflow.actvity;

@FunctionalInterface
public interface Activity<ViewType extends View>
{
    ViewType getView();
}
