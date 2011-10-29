/*
 * Copyright (c) 2011, Daniel Zwolenski. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
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
            throws FxmlLoadException
    {
        return (ControllerType)loadController(fxmlFile, (ResourceBundle)null);
    }

    @SuppressWarnings("unchecked")
    public <ControllerType extends Controller> ControllerType loadController(String fxmlFile, String resourceBundle)
            throws FxmlLoadException
    {
        return (ControllerType)loadController(fxmlFile, ResourceBundle.getBundle(resourceBundle));
    }

    @SuppressWarnings("unchecked")
    public <ControllerType extends Controller> ControllerType loadController(String fxmlFile, ResourceBundle resources)
            throws FxmlLoadException
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
                    log.warn("Error closing FXML stream", e);
                }
            }
        }
    }
}
