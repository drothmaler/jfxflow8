package com.zenjava.jfxflow;

import com.zenjava.jfxflow.controller.Browser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestApp extends Application
{
    public static void main(String[] args)
    {
        Application.launch(TestApp.class, args);
    }

    public void start(Stage stage) throws Exception
    {
        BorderPane root = new BorderPane();
        Browser browser = new Browser("Test");
        browser.getDockBarItems().add(new Button("Test"));
        root.setCenter(browser);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}