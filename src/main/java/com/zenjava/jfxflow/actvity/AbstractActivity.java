package com.zenjava.jfxflow.actvity;

import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;

public abstract class AbstractActivity<ViewType extends View>
        implements InjectedView<ViewType>, Activatable, HasWorkers, Releasable
{
    private final BooleanProperty active;
    private final BooleanProperty released;
    private final ListProperty<Worker> workers;

    private ViewType view;

    protected AbstractActivity()
    {
        this.active = new SimpleBooleanProperty();
        this.released = new SimpleBooleanProperty();
        this.active.addListener((source, oldValue, newValue) -> {
            if (newValue)
            {
                activated();
            }
            else
            {
                deactivated();
            }
        });

        this.workers = new SimpleListProperty<>();
    }

    @Override
    public ViewType getView()
    {
        return view;
    }

    @Override
    public void setView(ViewType view)
    {
        this.view = view;
    }

    @Override
    public BooleanProperty activeProperty()
    {
        return this.active;
    }

    @Override
    public void release()
    {
        setActive(false);
        released.set(true);
    }

    @Override
    public ReadOnlyBooleanProperty releasedProperty()
    {
        return released;
    }

    @Override
    public ListProperty<Worker> workersProperty()
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
        worker.runningProperty().addListener((source, olValue, newValue) -> {
            if (newValue)
            {
                workers.add(worker);
            }
            else
            {
                workers.remove(worker);
            }
        });
    }
}
