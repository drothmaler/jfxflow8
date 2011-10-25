package com.zenjava.demo.login;

import com.zenjava.demo.home.HomePlace;
import com.zenjava.demo.service.DemoService;
import com.zenjava.demo.service.LoginException;
import com.zenjava.jfxflow.controller.AbstractController;
import com.zenjava.jfxflow.navigation.NavigationManager;
import com.zenjava.jfxflow.worker.BackgroundWorker;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AbstractController
{
    @FXML private Label messageLabel;
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;

    private ResourceBundle resources;
    private DemoService remoteDemoService;
    private BackgroundWorker<String> loginWorker;
    private NavigationManager navigationManager;

    public void initialize(URL url, ResourceBundle resources)
    {
        this.resources = resources;
    }

    public void setNavigationManager(NavigationManager navigationManager)
    {
        this.navigationManager = navigationManager;
    }

    public void setRemoteDemoService(DemoService remoteDemoService)
    {
        this.remoteDemoService = remoteDemoService;
    }

    @FXML protected void doLogin(ActionEvent event)
    {
        if (loginWorker == null)
        {
            loginWorker = new BackgroundWorker<String>(busyProperty())
            {
                protected Task createTask()
                {
                    final String userName = userNameField.getText();
                    final String password = passwordField.getText();
                    return new Task<String>()
                    {
                        protected String call() throws Exception
                        {
                            return remoteDemoService.login(userName, password);
                        }
                    };
                }

                protected void onSuccess(String value)
                {
                    navigationManager.goTo(new HomePlace());
                }

                protected void onError(Throwable exception)
                {
                    if (exception instanceof LoginException)
                    {
                        messageLabel.setVisible(true);
                        messageLabel.setText(resources.getString("invalidLogin"));
                    }
                    else
                    {
                        super.onError(exception);
                    }
                }
            };
        }
        this.messageLabel.setVisible(false);
        this.loginWorker.cancel();
        this.loginWorker.restart();
    }
}
