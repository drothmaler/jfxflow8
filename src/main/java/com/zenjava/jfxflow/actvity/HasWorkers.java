package com.zenjava.jfxflow.actvity;

import javafx.beans.property.ListProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;

@FunctionalInterface
public interface HasWorkers
{
    ListProperty<Worker> workersProperty();

    default ObservableList<Worker> getWorkers() {
        return workersProperty().get();
    }
}
