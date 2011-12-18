package com.zenjava.jfxflow.actvity;

public interface InjectedView<ViewType extends View> extends Activity<ViewType>
{
    void setView(ViewType view);
}
