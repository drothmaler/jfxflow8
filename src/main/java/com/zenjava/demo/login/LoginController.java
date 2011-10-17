package com.zenjava.demo.login;

import com.zenjava.demo.home.HomeController;
import com.zenjava.demo.service.DemoService;
import com.zenjava.demo.service.LoginException;
import com.zenjava.jfxflow.AbstractController;
import com.zenjava.jfxflow.BackgroundWorker;
import com.zenjava.jfxflow.ControlManager;
import com.zenjava.jfxflow.Location;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AbstractController
{
    public static final String LOCATION = LoginController.class.getName();

    @FXML private Node rootNode;
    @FXML private Label messageLabel;
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;

    private ResourceBundle resources;
    private DemoService remoteDemoService;
    private BackgroundWorker<String> loginWorker;
    private ControlManager controlManager;

    public Node getRootNode()
    {
        return rootNode;
    }

    public void initialize(URL url, ResourceBundle resources)
    {
        this.resources = resources;
    }

    public void setControlManager(ControlManager controlManager)
    {
        this.controlManager = controlManager;
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
                    controlManager.goTo(new Location(HomeController.LOCATION));
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
