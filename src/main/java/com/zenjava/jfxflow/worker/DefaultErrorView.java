package com.zenjava.jfxflow.worker;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DefaultErrorView extends BorderPane
{
    private Label titleLabel;
    private TextArea detailArea;

    public DefaultErrorView()
    {
        titleLabel = new Label("Sorry, something went wrong.");
        BorderPane.setMargin(titleLabel, new Insets(10, 10, 10, 10));
        setTop(titleLabel);

        detailArea = new TextArea();
        detailArea.setEditable(false);
        BorderPane.setMargin(detailArea, new Insets(10, 10, 10, 10));
        setCenter(detailArea);
    }

    public void setError(Throwable error)
    {
        if (error != null)
        {
            StringWriter result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            error.printStackTrace(printWriter);
            detailArea.setText(result.toString());
        }
        else
        {
            detailArea.setText("(no detail available)");
        }
    }
}
