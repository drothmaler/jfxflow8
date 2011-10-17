package com.zenjava.demo.service;

public class DemoServiceImpl implements DemoService
{
    public String login(String userName, String password) throws LoginException
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            // ignore
        }

        if (userName.equals("guest"))
        {
            return "Guest User";
        }
        else
        {
            throw new LoginException("Invalid username or password");
        }
    }
}
