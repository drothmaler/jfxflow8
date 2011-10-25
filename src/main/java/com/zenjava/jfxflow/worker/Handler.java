package com.zenjava.jfxflow.worker;

public interface Handler<TargetType>
{
    void handle(TargetType target);
}
