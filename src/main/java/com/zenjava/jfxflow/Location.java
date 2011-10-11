package com.zenjava.jfxflow;

public class Location<DataType>
{
    private String id;
    private DataType data;

    public Location(String id)
    {
        this.id = id;
    }

    public Location(String id, DataType data)
    {
        this.id = id;
        this.data = data;
    }

    public String getId()
    {
        return id;
    }

    public DataType getData()
    {
        return data;
    }
}
