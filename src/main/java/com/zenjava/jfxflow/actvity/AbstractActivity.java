package com.zenjava.jfxflow.actvity;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Node;

public abstract class AbstractActivity<NodeType extends Node>
        implements HasNode<NodeType>, Activatable, HasWorkers
{
    private NodeType node;
    private BooleanProperty active;
    private ObservableList<Worker> workers;

    protected AbstractActivity()
    {
        this.active = new SimpleBooleanProperty();
        this.active.addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> source, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    activated();
                }
                else
                {
                    deactivated();
                }
            }
        });

        this.workers = FXCollections.observableArrayList();
    }

    public NodeType getNode()
    {
        return node;
    }

    protected void setNode(NodeType node)
    {
        this.node = node;
    }

    public void setActive(boolean active)
    {
        this.active.set(active);
    }

    public boolean isActive()
    {
        return this.active.get();
    }

    public BooleanProperty activeProperty()
    {
        return this.active;
    }

    public ObservableList<Worker> getWorkers()
    {
        return workers;
    }

    protected void activated()
    {
    }

    protected void deactivated()
    {
    }

    protected void executeTask(Task task)
    {
        watchWorker(task);
        new Thread(task).start();
    }

    protected void watchWorker(final Worker worker)
    {
        worker.runningProperty().addListener(new ChangeListener<Boolean>()
        {
            public void changed(ObservableValue<? extends Boolean> source, Boolean olValue, Boolean newValue)
            {
                if (newValue)
                {
                    workers.add(worker);
                }
                else
                {
                    workers.remove(worker);
                }
            }
        });
    }
}
