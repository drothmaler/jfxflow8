package com.zenjava.jfxflow.actvity;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker;

@FunctionalInterface
public interface HasWorkers
{
    ObservableList<Worker> getWorkers();
}
