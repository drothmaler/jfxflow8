package com.zenjava.jfxflow.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class FxmlControllerLoader
{
    private static final Logger log = LoggerFactory.getLogger(FxmlControllerLoader.class);

    @SuppressWarnings("unchecked")
    public <ControllerType extends Controller> ControllerType loadController(String fxmlFile)
    {
        return (ControllerType)loadController(fxmlFile, (ResourceBundle)null);
    }

    @SuppressWarnings("unchecked")
    public <ControllerType extends Controller> ControllerType loadController(String fxmlFile, String resourceBundle)
    {
        return (ControllerType)loadController(fxmlFile, ResourceBundle.getBundle(resourceBundle));
    }

    @SuppressWarnings("unchecked")
    public <ControllerType extends Controller> ControllerType loadController(String fxmlFile, ResourceBundle resources)
    {
        log.debug("Loading controller from FXML '{}'", fxmlFile);

        InputStream fxmlStream = null;
        try
        {
            fxmlStream = getClass().getResourceAsStream(fxmlFile);
            FXMLLoader loader = new FXMLLoader();
            Node view = (Node) loader.load(fxmlStream);
            if (resources != null)
            {
                loader.setResources(resources);
            }

            ControllerType controller = (ControllerType) loader.getController();
            if (controller instanceof HasFxmlLoadedView)
            {
                ((HasFxmlLoadedView) controller).setView(view);
            }
            return controller;
        }
        catch (IOException e)
        {
            // map checked exception to a runtime exception - this is a system failure, not a business logic failure
            // so using a checked exceptions for this is not necessary.
            throw new FxmlLoadException(String.format(
                    "Unable to load FXML from '%s': %s", fxmlFile, e.getMessage()), e);
        }
        finally
        {
            if (fxmlStream != null)
            {
                try
                {
                    fxmlStream.close();
                }
                catch (IOException e)
                {
                    log.warn("Error closing FXML stream", e);
                }
            }
        }
    }
}
