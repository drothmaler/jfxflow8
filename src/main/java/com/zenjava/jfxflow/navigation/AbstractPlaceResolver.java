package com.zenjava.jfxflow.navigation;

import com.zenjava.jfxflow.actvity.Activity;
import com.zenjava.jfxflow.actvity.ActivityParameterException;
import com.zenjava.jfxflow.actvity.Param;
import javafx.beans.value.WritableValue;

import java.lang.reflect.Field;

public abstract class AbstractPlaceResolver implements PlaceResolver
{
    public Activity resolvePlace(Place place)
    {
        Activity activity = findActivity(place);
        if (activity != null)
        {
            setParameters(activity, place);
        }
        return activity;
    }

    protected abstract Activity findActivity(Place place);

    protected void setParameters(Activity activity, Place place)
    {
        for (Field field : activity.getClass().getDeclaredFields())
        {
            Param annotation = field.getAnnotation(Param.class);
            if (annotation != null)
            {
                String name = annotation.value();
                if (name == null || name.equals(""))
                {
                    name = field.getName();
                }

                Object value = place.getParameters().get(name);

                try
                {
                    field.setAccessible(true);
                    if (WritableValue.class.isAssignableFrom(field.getType()))
                    {
                        WritableValue property = (WritableValue) field.get(activity);
                        property.setValue(value);
                    }
                    else
                    {
                        field.set(activity, value);
                    }
                }
                catch (IllegalAccessException e)
                {
                    throw new ActivityParameterException(
                            String.format("Error setting property '%s' on field '%s' in Activity '%s'",
                                    name, field.getName(), activity), e);
                }
            }
        }
    }
}
