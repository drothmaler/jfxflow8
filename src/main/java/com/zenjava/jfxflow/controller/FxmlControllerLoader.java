package com.zenjava.jfxflow.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

public class FxmlControllerLoader
{
    private static final Logger log = LoggerFactory.getLogger(FxmlControllerLoader.class);

    public static <ControllerType> ControllerType loadController(Class<ControllerType> controllerClass,
                                                                 String fxmlFile,
                                                                 String resourceBundle)
    {
        return loadController(controllerClass, fxmlFile, ResourceBundle.getBundle(resourceBundle));
    }

    @SuppressWarnings("unchecked")
    public static <ControllerType> ControllerType loadController(Class<ControllerType> controllerClass,
                                                                 String fxmlFile,
                                                                 ResourceBundle resourceBundle)
    {
        try
        {
            log.debug("Loading controller '{}' from FXML '{}'", controllerClass.getSimpleName(), fxmlFile);
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(resourceBundle);
            Node view = (Node) loader.load(controllerClass.getResourceAsStream(fxmlFile));
            ControllerType controller = (ControllerType) loader.getController();
            if (controller instanceof HasFxmlLoadedView)
            {
                ((HasFxmlLoadedView)controller).setView(view);
            }
            return controller;
        }
        catch (Exception e)
        {
            // map checked exception to a runtime exception - this is a system failure, not a business logic failure,
            // using checked exceptions for this is old school.
            throw new RuntimeException(String.format(
                    "Unable to load '%s' from FXML '%s'", controllerClass.getSimpleName(), fxmlFile), e);
        }
    }
}
