package com.zenjava.jfxflow.control;

import com.sun.javafx.css.StyleManager;
import com.zenjava.jfxflow.actvity.Activatable;
import com.zenjava.jfxflow.actvity.HasNode;
import com.zenjava.jfxflow.actvity.Param;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;

public class ErrorPage extends BorderPane implements HasNode<ErrorPage>, Activatable
{
    // work around for bug: http://javafx-jira.kenai.com/browse/RT-16647
    static
    {
        StyleManager.getInstance().addUserAgentStylesheet(
                ErrorPage.class.getResource("jfxflow-error-page.css").toExternalForm());
    }

    @Param private ObjectProperty<Throwable> error;

    private BooleanProperty active;

    public ErrorPage()
    {
        getStyleClass().add("error-page");
        this.error = new SimpleObjectProperty<Throwable>();
        this.active = new SimpleBooleanProperty();
        setCenter(new ErrorPageSkin(this).getNode());
    }

    public ErrorPage getNode()
    {
        return this;
    }

    public void setActive(boolean active)
    {
        this.active.set(active);
    }

    public boolean isActive()
    {
        return this.active.get();
    }

    public BooleanProperty activeProperty()
    {
        return this.active;
    }

    public void setError(Throwable error)
    {
        this.error.set(error);
    }

    public Throwable getError()
    {
        return this.error.get();
    }

    public ObjectProperty<Throwable> errorProperty()
    {
        return this.error;
    }

    protected String getUserAgentStylesheet()
    {
        return ErrorPage.class.getResource("jfxflow-error-page.css").toExternalForm();
    }
}
