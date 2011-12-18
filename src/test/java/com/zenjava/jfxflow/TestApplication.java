package com.zenjava.jfxflow;

import com.zenjava.jfxflow.control.Browser;
import com.zenjava.jfxflow.control.DefaultBrowserHeader;
import com.zenjava.jfxflow.error.DefaultErrorHandler;
import com.zenjava.jfxflow.error.ErrorHandler;
import com.zenjava.jfxflow.navigation.Place;
import com.zenjava.jfxflow.navigation.RegexPlaceResolver;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.stage.Stage;

public class TestApplication extends Application
{
    public static void main(String[] args)
    {
        Application.launch(TestApplication.class, args);
    }

    public void start(Stage stage) throws Exception
    {
        Browser browser = new Browser();

        DefaultBrowserHeader header = new DefaultBrowserHeader(browser, "Test Application");
        header.getLeftDockBar().getChildren().addAll(
                new Separator(Orientation.VERTICAL), new Button("Another"));
        header.getRightDockBar().getChildren().addAll(new Button("Admin"));
        browser.setHeader(header);

        ErrorHandler errorHandler = new DefaultErrorHandler(browser.getNavigationManager());

        Page1Activity page1Activity = new Page1Activity(browser.getNavigationManager());
        browser.getPlaceResolvers().add(new RegexPlaceResolver("page1", page1Activity));

        Page2Activity page2Activity = new Page2Activity(browser.getNavigationManager(), errorHandler);
        browser.getPlaceResolvers().add(new RegexPlaceResolver("page2", page2Activity));

        TabbedParentActivity tabbedParentActivity = new TabbedParentActivity();
        browser.getPlaceResolvers().add(new RegexPlaceResolver("tabbed", tabbedParentActivity));

        browser.getNavigationManager().goTo(new Place("page1"));

        Scene scene = new Scene(browser, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
