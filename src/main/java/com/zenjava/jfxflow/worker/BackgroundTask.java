/*
 * Copyright (c) 2011, Daniel Zwolenski. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package com.zenjava.jfxflow.worker;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BackgroundTask<DataType> extends Task<DataType>
{
    private static final Logger log = LoggerFactory.getLogger(BackgroundTask.class);

    private SimpleObjectProperty<Handler<DataType>> onSuccess;
    private SimpleObjectProperty<ErrorHandler> onError;

    protected BackgroundTask()
    {
        this(null, null);
    }

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    protected BackgroundTask(BooleanProperty bindToRunningProperty, ErrorHandler errorHandler)
    {
        onSuccess = new SimpleObjectProperty<Handler<DataType>>();
        onError = new SimpleObjectProperty<ErrorHandler>();

        stateProperty().addListener(new ChangeListener<State>()
        {
            public void changed(ObservableValue<? extends State> source, State oldState, State newState)
            {
                if (newState.equals(State.SUCCEEDED))
                {
                    onSuccess(getValue());
                }
                else if (newState.equals(State.FAILED))
                {
                    onError(getException());
                }
            }
        });

        if (bindToRunningProperty != null)
        {
            bindToRunningProperty.bind(runningProperty());
        }

        setOnError(errorHandler);
    }

    public Handler<DataType> getOnSuccess()
    {
        return onSuccess.get();
    }

    public void setOnSuccess(Handler<DataType> successHandler)
    {
        this.onSuccess.set(successHandler);
    }

    public ErrorHandler getOnError()
    {
        return onError.get();
    }

    public void setOnError(ErrorHandler errorHandler)
    {
        this.onError.set(errorHandler);
    }

    protected void onSuccess(DataType value)
    {
        Handler<DataType> successHandler = getOnSuccess();
        if (successHandler != null)
        {
            successHandler.handle(value);
        }
    }

    protected void onError(Throwable exception)
    {
        ErrorHandler errorHandler = getOnError();
        if (errorHandler != null)
        {
            errorHandler.handleError(exception);
        }
        else
        {
            log.error("Unhandled error in background worker", exception);
        }
    }
}
