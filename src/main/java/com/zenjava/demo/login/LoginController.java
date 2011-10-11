package com.zenjava.demo.login;

import com.zenjava.demo.home.HomeController;
import com.zenjava.jfxflow.AbstractController;
import com.zenjava.jfxflow.ControlManager;
import com.zenjava.jfxflow.Location;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class LoginController extends AbstractController
{
    public static final String LOCATION = LoginController.class.getName();

    @FXML private Node rootNode;
    @FXML private TextField userNameField;
    @FXML private TextField passwordField;

    private LoginService loginService;
    private ControlManager controlManager;

    public LoginController()
    {
        this.loginService = new LoginService();
    }

    public Node getRootNode()
    {
        return rootNode;
    }

    public void setControlManager(ControlManager controlManager)
    {
        this.controlManager = controlManager;
    }

    @FXML protected void doLogin(ActionEvent event)
    {
        this.loginService.cancel();
        this.loginService.restart();
    }

    //-------------------------------------------------------------------------

    public class LoginService extends Service<String>
    {
        public LoginService()
        {
            stateProperty().addListener(new ChangeListener<State>()
            {
                public void changed(ObservableValue<? extends State> source, State oldState, State newState)
                {
                    if (newState.equals(State.SUCCEEDED))
                    {
                        controlManager.goTo(new Location(HomeController.LOCATION));
                    }
                    else if (newState.equals(State.FAILED))
                    {
                        // todo handle error
                    }
                }
            });
        }

        protected Task createTask()
        {
            final String userName = userNameField.getText();
            final String password = passwordField.getText();
            return new Task<String>()
            {
                protected String call() throws Exception
                {
                    try { Thread.sleep(2000); } catch (InterruptedException e) { }
//                    String displayName = remoteLoginService.doLogin(userName, password);
                    String displayName = userName;
//                    return displayName;
                    throw new Exception("TEST");
                }
            };
        }
    }
}
