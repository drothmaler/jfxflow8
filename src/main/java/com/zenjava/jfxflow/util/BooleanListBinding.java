package com.zenjava.jfxflow.util;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BooleanListBinding extends BooleanBinding
{
    private BooleanOperator operator;
    private ObservableList<ObservableBooleanValue> booleanValues;

    public BooleanListBinding(BooleanOperator operator)
    {
        this.operator = operator;
        booleanValues = FXCollections.observableArrayList();
        bind(booleanValues);
    }

    public ObservableList<ObservableBooleanValue> getBooleanValues()
    {
        return booleanValues;
    }

    protected boolean computeValue()
    {
        switch (operator)
        {
            case and:
                for (ObservableBooleanValue value : booleanValues)
                {
                    if (!value.get())
                    {
                        return false;
                    }
                }
                return booleanValues.size() > 0;

            case or:
                for (ObservableBooleanValue value : booleanValues)
                {
                    if (value.get())
                    {
                        return true;
                    }
                }
                return false;

            default:
                throw new IllegalArgumentException("Unsupported operator for Boolean list: " + operator);
        }
    }
}
