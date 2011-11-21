package com.zenjava.jfxflow;

import com.zenjava.jfxflow.control.Browser;
import com.zenjava.jfxflow.control.BrowserHeader;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.RegexPlaceResolver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.stage.Stage;

import java.util.Arrays;

public class TestApplication extends Application
{
    public static void main(String[] args)
    {
        Application.launch(TestApplication.class, args);
    }

    public void start(Stage stage) throws Exception
    {
        Browser browser = new Browser();
        browser.setHeader(new BrowserHeader(browser, "Test Application",
                Arrays.asList(new Separator(), new Button("Another")),
                Arrays.asList(new Button("Admin"))
                ));

        Page1Activity page1Activity = new Page1Activity(browser.getNavigationManager());
        browser.getPlaceResolvers().add(new RegexPlaceResolver("page1", page1Activity));
        Page2Activity page2Activity = new Page2Activity(browser.getNavigationManager());
        browser.getPlaceResolvers().add(new RegexPlaceResolver("page2", page2Activity));
        browser.getNavigationManager().goTo(new Place("page1"));

        Scene scene = new Scene(browser, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
