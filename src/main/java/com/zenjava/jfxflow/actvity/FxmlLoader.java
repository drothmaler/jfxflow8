package com.zenjava.jfxflow.actvity;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.util.Callback;

import java.io.InputStream;
import java.util.Map;
import java.util.ResourceBundle;

public class FxmlLoader
{

    private Callback<Class<?>, Object> controllerFactory;

    public void setControllerFactory(Callback<Class<?>, Object> controllerFactory) {
        this.controllerFactory = controllerFactory;
    }

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
        try (InputStream fxmlStream = getClass().getResourceAsStream(fxmlFile))
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setBuilderFactory(new JavaFXBuilderFactory());

            loader.setLocation(getClass().getResource(fxmlFile));

            if (this.controllerFactory != null)
            {
                loader.setControllerFactory(this.controllerFactory);
            }

            if (resources != null)
            {
                loader.setResources(resources);
            }

            if (variables != null)
            {
                loader.getNamespace().putAll(variables);
            }

            Node rootNode = loader.load(fxmlStream);

            Type controller = loader.getController();
            if (controller instanceof InjectedView)
            {
                final InjectedView injectedView = (InjectedView) controller;
                final View view = rootNode instanceof View ? (View) rootNode : new SimpleView(rootNode);
                injectedView.setView(view);
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
    }
}
