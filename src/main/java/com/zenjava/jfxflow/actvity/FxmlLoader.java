package com.zenjava.jfxflow.actvity;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;

public class FxmlLoader
{
    @SuppressWarnings("unchecked")
    public <Type extends Activity> Type load(String fxmlFile)
            throws FxmlLoadException
    {
        return (Type) load(fxmlFile, null, null);
    }

    @SuppressWarnings("unchecked")
    public <Type extends Activity> Type load(String fxmlFile, String resourceBundle)
            throws FxmlLoadException
    {
        return (Type) load(fxmlFile, ResourceBundle.getBundle(resourceBundle), null);
    }

    @SuppressWarnings("unchecked")
    public <Type extends Activity> Type load(String fxmlFile, ResourceBundle resourceBundle)
            throws FxmlLoadException
    {
        return (Type) load(fxmlFile, resourceBundle, null);
    }

    @SuppressWarnings("unchecked")
    public <Type extends Activity> Type load(String fxmlFile, ResourceBundle resources, Map<String, Object> variables)
            throws FxmlLoadException
    {
        InputStream fxmlStream = null;
        try
        {
            fxmlStream = getClass().getResourceAsStream(fxmlFile);
            FXMLLoader loader = new FXMLLoader();
            loader.setBuilderFactory(new JavaFXBuilderFactory());

            loader.setLocation(getClass().getResource(fxmlFile));

            if (resources != null)
            {
                loader.setResources(resources);
            }

            if (variables != null)
            {
                loader.getNamespace().putAll(variables);
            }

            Node rootNode = (Node) loader.load(fxmlStream);

            Type controller = (Type) loader.getController();
            if (controller instanceof InjectedView)
            {
                View view;
                if (rootNode instanceof View)
                {
                    ((InjectedView) controller).setView((View) rootNode);
                }
                else
                {
                    ((InjectedView) controller).setView(new SimpleView(rootNode));
                }
            }
            return controller;
        }
        catch (Exception e)
        {
            // map checked exception to a runtime exception - this is a system failure, not a business logic failure
            // so using checked exceptions for this is not necessary.
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
                    System.err.println("WARNING: error closing FXML stream: " + e);
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
