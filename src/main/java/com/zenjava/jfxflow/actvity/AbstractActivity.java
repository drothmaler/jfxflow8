package com.zenjava.jfxflow.actvity;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

public abstract class AbstractActivity<ViewType extends View>
        implements InjectedView<ViewType>, Activatable, HasWorkers, Releasable
{
    private ViewType view;
    private BooleanProperty active;
    private BooleanProperty released;
    private ObservableList<Worker> workers;

    protected AbstractActivity()
    {
        this.active = new SimpleBooleanProperty();
        this.released = new SimpleBooleanProperty();
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

    public ViewType getView()
    {
        return view;
    }

    public void setView(ViewType view)
    {
        this.view = view;
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

    public void release()
    {
        setActive(false);
        released.set(true);
    }

    public ReadOnlyBooleanProperty releasedProperty()
    {
        return released;
    }

    public boolean isReleased()
    {
        return released.get();
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
