package com.zenjava.jfxflow.worker;

import com.zenjava.jfxflow.controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultErrorController extends AbstractController<DefaultErrorView, ErrorPlace>
{
    private static final Logger log = LoggerFactory.getLogger(DefaultErrorController.class);

    public DefaultErrorController()
    {
        setView(new DefaultErrorView());
    }

    public void activate(ErrorPlace place)
    {
        log.error("Showing error", place.getError());
        getView().setError(place.getError());
    }
}
