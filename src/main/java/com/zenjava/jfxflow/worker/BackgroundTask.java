package com.zenjava.jfxflow.worker;

import com.zenjava.jfxflow.error.ErrorHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

public abstract class BackgroundTask<ResultType> extends Task<ResultType>
{
    private final ObjectProperty<ErrorHandler> errorHandler;

    protected BackgroundTask()
    {
        this(null);
    }

    protected BackgroundTask(ErrorHandler errorHandler)
    {
        this.errorHandler = new SimpleObjectProperty<>(errorHandler);

        stateProperty().addListener((source, oldState, newState) -> {
            if (newState.equals(State.SUCCEEDED))
            {
                onSuccess(getValue());
            }
            else if (newState.equals(State.FAILED))
            {
                onError(getException());
            }
        });
    }

    public ObjectProperty<ErrorHandler> errorHandlerProperty()
    {
        return errorHandler;
    }

    public ErrorHandler getErrorHandler()
    {
        return errorHandler.get();
    }

    public void setErrorHandler(ErrorHandler errorHandler)
    {
        this.errorHandler.set(errorHandler);
    }

    protected void onSuccess(ResultType value)
    {
    }

    protected void onError(Throwable exception)
    {
        ErrorHandler handler = errorHandler.get();
        if (handler != null)
        {
            handler.handleError(exception);
        }
        else
        {
            throw new UnhandledWorkerException("Error in background task", exception);
        }
    }
}
