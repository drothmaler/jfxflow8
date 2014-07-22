package com.zenjava.jfxflow.transition;

@FunctionalInterface
public interface HasExitTransition
{
    ViewTransition getExitTransition();
}
