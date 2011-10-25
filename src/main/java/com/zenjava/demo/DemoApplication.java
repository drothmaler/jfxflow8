package com.zenjava.demo;

import com.zenjava.demo.login.LoginPlace;
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
        stage.setScene(factory.mainScene());
        factory.navigationManager().goTo(new LoginPlace());
        stage.show();
    }
}
