package com.zenjava.demo;

import com.zenjava.demo.home.HomeController;
import com.zenjava.demo.home.HomePlace;
import com.zenjava.demo.login.LoginController;
import com.zenjava.demo.login.LoginPlace;
import com.zenjava.demo.search.SearchController;
import com.zenjava.demo.search.SearchPlace;
import com.zenjava.demo.service.DemoService;
import com.zenjava.demo.service.DemoServiceImpl;
import com.zenjava.jfxflow.controller.Browser;
import com.zenjava.jfxflow.controller.FxmlControllerLoader;
import com.zenjava.jfxflow.navigation.DefaultNavigationManager;
import com.zenjava.jfxflow.navigation.NavigationManager;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class DemoUiFactory
{
    private DemoService demoService;
    private DefaultNavigationManager navigationManager;
    private Browser mainBrowser;
    private LoginController loginController;
    private HomeController homeController;
    private SearchController searchController;

    public Scene mainScene()
    {
        BorderPane contentArea = new BorderPane();
        contentArea.setCenter(mainBrowser());
        Scene scene = new Scene(contentArea, 800, 600);
        scene.getStylesheets().add("styles/demo-style.css");
        return scene;
    }

    public DemoService demoService()
    {
        if (demoService == null)
        {
            demoService = new DemoServiceImpl();
        }
        return demoService;
    }

    public NavigationManager navigationManager()
    {
        if (navigationManager == null)
        {
            navigationManager = new DefaultNavigationManager();
        }
        return navigationManager;
    }

    public Browser mainBrowser()
    {
        if (mainBrowser == null)
        {
            mainBrowser = new Browser("JFX Flow Demo", navigationManager(), new HomePlace());
            mainBrowser.registerController(LoginPlace.class, loginController());
            mainBrowser.registerController(HomePlace.class, homeController());
            mainBrowser.registerController(SearchPlace.class, searchController());
        }
        return mainBrowser;
    }

    public LoginController loginController()
    {
        if (loginController == null)
        {
            loginController = FxmlControllerLoader.loadController(LoginController.class, "/fxml/login.fxml", "messages/login");
            loginController.setNavigationManager(navigationManager());
            loginController.setRemoteDemoService(demoService());
        }
        return loginController;
    }

    public HomeController homeController()
    {
        if (homeController == null)
        {
            homeController = FxmlControllerLoader.loadController(HomeController.class, "/fxml/home.fxml", "messages/home");
            homeController.setNavigationManager(navigationManager());
        }
        return homeController;
    }

    public SearchController searchController()
    {
        if (searchController == null)
        {
            searchController = FxmlControllerLoader.loadController(SearchController.class, "/fxml/search.fxml", "messages/search");
        }
        return searchController;
    }
}
