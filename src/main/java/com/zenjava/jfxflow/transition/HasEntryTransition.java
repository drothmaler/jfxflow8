package com.zenjava.jfxflow.transition;

public interface HasEntryTransition
{
    boolean isSequentialTransition();

    ViewTransition getEntryTransition();
}
