package com.zenjava.jfxflow.error;

@FunctionalInterface
public interface ErrorHandler
{
    void handleError(Throwable error);
}
