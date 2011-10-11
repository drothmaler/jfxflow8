package com.zenjava.demo;

import com.zenjava.demo.login.LoginController;
import com.zenjava.jfxflow.Location;
import javafx.application.Application;
import javafx.stage.Stage;

public class DemoApplication extends Application
{
    public static void main(String[] args) throws Exception
    {
        launch(args);
    }

    public void start(Stage stage) throws Exception
    {
        DemoUiFactory factory = new DemoUiFactory();
        factory.controlManager().goTo(new Location(LoginController.LOCATION));
        stage.setScene(factory.mainScene());
        stage.show();
    }
}
