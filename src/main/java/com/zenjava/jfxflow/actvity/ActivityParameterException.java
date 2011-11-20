package com.zenjava.jfxflow.actvity;

public class ActivityParameterException extends RuntimeException
{
    public ActivityParameterException(String message)
    {
        super(message);
    }

    public ActivityParameterException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
