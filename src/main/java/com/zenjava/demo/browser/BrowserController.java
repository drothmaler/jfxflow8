package com.zenjava.demo.browser;

import com.zenjava.demo.home.HomeController;
import com.zenjava.demo.login.LoginController;
import com.zenjava.jfxflow.AbstractController;
import com.zenjava.jfxflow.ControlManager;
import com.zenjava.jfxflow.Controller;
import com.zenjava.jfxflow.Location;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BrowserController extends AbstractController
{
    @FXML private Node rootNode;
    @FXML private Node glassPane;
    @FXML private StackPane contentArea;
    @FXML private Pane navButtonArea;
    @FXML private Button homeButton;
    @FXML private Button refreshButton;
    @FXML private Button backButton;
    @FXML private Button forwardButton;

    private ControlManager controlManager;
    private ListChangeListener<Location> historyListener;
    private ChangeListener<Number> currentPlaceInHistoryListener;
    private ChangeListener<Controller> currentControllerListener;

    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.historyListener = new ListChangeListener<Location>()
        {
            public void onChanged(Change<? extends Location> change)
            {
                updateButtonStates();
            }
        };

        this.currentPlaceInHistoryListener = new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> source, Number oldValue, Number newValue)
            {
                updateButtonStates();
            }
        };

        this.currentControllerListener = new ChangeListener<Controller>()
        {
            public void changed(ObservableValue<? extends Controller> source, Controller oldValue, Controller newValue)
            {
                busyProperty().unbind();
                if (newValue != null)
                {
                    busyProperty().bind(newValue.busyProperty());
                }
            }
        };

        glassPane.visibleProperty().bind(busyProperty());
    }

    public void setControlManager(ControlManager controlManager)
    {
        if (this.controlManager != null)
        {
            busyProperty().unbind();
            this.controlManager.getHistory().removeListener(historyListener);
            this.controlManager.currentPlaceInHistoryProperty().removeListener(currentPlaceInHistoryListener);
            this.controlManager.currentControllerProperty().removeListener(currentControllerListener);
        }

        this.controlManager = controlManager;

        if (this.controlManager != null)
        {
            this.controlManager.getHistory().addListener(historyListener);
            this.controlManager.currentPlaceInHistoryProperty().addListener(currentPlaceInHistoryListener);
            this.controlManager.currentControllerProperty().addListener((currentControllerListener));
            Controller currentController = this.controlManager.currentControllerProperty().get();
            if (currentController != null)
            {
                busyProperty().bind(currentController.busyProperty());
            }
        }
        updateButtonStates();
    }

    public Node getRootNode()
    {
        return rootNode;
    }

    public StackPane getContentArea()
    {
        return contentArea;
    }

    @FXML protected void homeSelected(ActionEvent event)
    {
        controlManager.goTo(new Location(HomeController.LOCATION));
    }

    @FXML protected void refreshSelected(ActionEvent event)
    {
        controlManager.refresh();
    }

    @FXML protected void backSelected(ActionEvent event)
    {
        controlManager.goBack();
    }

    @FXML protected void forwardSelected(ActionEvent event)
    {
        controlManager.goForward();
    }

    protected void updateButtonStates()
    {
        ObservableList<Location> history = controlManager.getHistory();
        int currentPlace = controlManager.currentPlaceInHistoryProperty().get();
        backButton.setDisable(currentPlace == 0);
        forwardButton.setDisable(currentPlace >= history.size() - 1);

        boolean showNavButtons = false;
        if (currentPlace < controlManager.getHistory().size())
        {
            Location location = controlManager.getHistory().get(currentPlace);
            showNavButtons = !location.getId().equals(LoginController.LOCATION);
        }
        navButtonArea.setVisible(showNavButtons);
    }
}
