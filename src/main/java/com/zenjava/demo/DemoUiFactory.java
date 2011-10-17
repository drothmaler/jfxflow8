package com.zenjava.demo;

import com.zenjava.demo.browser.BrowserController;
import com.zenjava.demo.home.HomeController;
import com.zenjava.demo.login.LoginController;
import com.zenjava.demo.search.SearchController;
import com.zenjava.demo.service.DemoService;
import com.zenjava.demo.service.DemoServiceImpl;
import com.zenjava.jfxflow.ControlManager;
import com.zenjava.jfxflow.DefaultControlManager;
import com.zenjava.jfxflow.FxmlHelper;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class DemoUiFactory
{
    private DemoService demoService;
    private DefaultControlManager controlManager;
    private BrowserController mainBrowser;
    private LoginController loginController;
    private HomeController homeController;
    private SearchController searchController;

    public Scene mainScene()
    {
        BorderPane contentArea = new BorderPane();
        contentArea.setCenter(mainBrowser().getRootNode());
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

    public ControlManager controlManager()
    {
        if (controlManager == null)
        {
            BrowserController mainBrowser = mainBrowser();
            controlManager = new DefaultControlManager(mainBrowser.getContentArea());
            controlManager.register(LoginController.LOCATION, loginController());
            controlManager.register(HomeController.LOCATION, homeController());
            controlManager.register(SearchController.LOCATION, searchController());
            mainBrowser.setControlManager(controlManager);
        }
        return controlManager;
    }

    public BrowserController mainBrowser()
    {
        if (mainBrowser == null)
        {
            mainBrowser = FxmlHelper.loadController(BrowserController.class, "/fxml/browser.fxml", "messages/browser");
        }
        return mainBrowser;
    }

    public LoginController loginController()
    {
        if (loginController == null)
        {
            loginController = FxmlHelper.loadController(LoginController.class, "/fxml/login.fxml", "messages/login");
            loginController.setControlManager(controlManager());
            loginController.setRemoteDemoService(demoService());
        }
        return loginController;
    }

    public HomeController homeController()
    {
        if (homeController == null)
        {
            homeController = FxmlHelper.loadController(HomeController.class, "/fxml/home.fxml", "messages/home");
            homeController.setControlManager(controlManager());
        }
        return homeController;
    }

    public SearchController searchController()
    {
        if (searchController == null)
        {
            searchController = FxmlHelper.loadController(SearchController.class, "/fxml/search.fxml", "messages/search");
        }
        return searchController;
    }
}
