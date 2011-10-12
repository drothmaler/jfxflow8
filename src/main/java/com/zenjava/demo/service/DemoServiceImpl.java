package com.zenjava.demo.service;

public class DemoServiceImpl implements DemoService
{
    public String login(String userName, String password)
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            // ignore
        }
        return userName;
    }
}
