package com.zenjava.jfxflow.dialog;

public class NoDialogOwnerException extends RuntimeException
{
    public NoDialogOwnerException(String message)
    {
        super(message);
    }

    public NoDialogOwnerException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
