package com.zenjava.demo.service;

public class LoginException extends Exception
{
    public LoginException(String message)
    {
        super(message);
    }

    public LoginException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
