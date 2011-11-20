package com.zenjava.jfxflow.navigation;

import java.util.HashMap;
import java.util.Map;

public class Place
{
    private String name;
    private Map<String, Object> parameters;

    public Place(String name)
    {
        this(name, null);
    }

    public Place(String name, Map<String, Object> parameters)
    {
        this.name = name;
        this.parameters = new HashMap<String, Object>();
        if (parameters != null)
        {
            this.parameters.putAll(parameters);
        }
    }

    public String getName()
    {
        return name;
    }

    public Map<String, Object> getParameters()
    {
        return parameters;
    }

    public String toString()
    {
        return String.format("Place[%s]", name);
    }
}
