package com.zenjava.jfxflow.navigation;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

public class Place
{
    private StringProperty name;
    private Map<String, Object> parameters;

    public Place(String name)
    {
        this(name, null);
    }

    public Place(String name, Map<String, Object> parameters)
    {
        this.name = new SimpleStringProperty(name);
        this.parameters = new HashMap<String, Object>();
        if (parameters != null)
        {
            this.parameters.putAll(parameters);
        }
    }

    public StringProperty nameProperty()
    {
        return this.name;
    }

    public String getName()
    {
        return this.name.get();
    }

    public void setName(String name)
    {
        this.name.set(name);
    }

    public Map<String, Object> getParameters()
    {
        return parameters;
    }

    public String toString()
    {
        return String.format("Place[%s]", getName());
    }
}
