package com.zenjava.jfxflow.navigation;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TestDefaultNavigationManager
{
    @Test
    public void testNavigation()
    {
        DefaultNavigationManager navigationManager = new DefaultNavigationManager();

        assertNull("Current place should start as null", navigationManager.getCurrentPlace());
        assertTrue("Back history should be empty at start", navigationManager.getBackHistory().size() == 0);
        assertTrue("Forward history should be empty at start", navigationManager.getForwardHistory().size() == 0);

        // add place1

        Place place1 = new Place("place1");
        navigationManager.goTo(place1);
        assertEquals("Current place not as expected", place1, navigationManager.getCurrentPlace());
        assertTrue("Back history should be empty", navigationManager.getBackHistory().size() == 0);
        assertTrue("Forward history should be empty", navigationManager.getForwardHistory().size() == 0);

        // add place2

        Place place2 = new Place("place2");
        navigationManager.goTo(place2);
        assertEquals("Current place not as expected", place2, navigationManager.getCurrentPlace());
        assertEquals("Back history should have a place in it", 1, navigationManager.getBackHistory().size());
        assertEquals("Back history has incorrect elements", Arrays.asList(place1), navigationManager.getBackHistory());
        assertTrue("Forward history should be empty", navigationManager.getForwardHistory().size() == 0);

        // add place3

        Place place3 = new Place("place3");
        navigationManager.goTo(place3);
        assertEquals("Current place not as expected", place3, navigationManager.getCurrentPlace());
        assertEquals("Back history should have 2 places in it", 2, navigationManager.getBackHistory().size());
        assertEquals("Back history has incorrect elements", Arrays.asList(place1, place2), navigationManager.getBackHistory());
        assertTrue("Forward history should be empty", navigationManager.getForwardHistory().size() == 0);

        // go back

        navigationManager.goBack();
        assertEquals("Current place not as expected", place2, navigationManager.getCurrentPlace());
        assertEquals("Back history should have 1 place in it", 1, navigationManager.getBackHistory().size());
        assertEquals("Back history has incorrect elements", Arrays.asList(place1), navigationManager.getBackHistory());
        assertEquals("Forward history should have 1 place in it", 1, navigationManager.getForwardHistory().size());
        assertEquals("Forward history has incorrect elements", Arrays.asList(place3), navigationManager.getForwardHistory());

        // go back (again)

        navigationManager.goBack();
        assertEquals("Current place not as expected", place1, navigationManager.getCurrentPlace());
        assertTrue("Back history should be empty", navigationManager.getBackHistory().size() == 0);
        assertEquals("Forward history should have 2 places in it", 2, navigationManager.getForwardHistory().size());
        assertEquals("Forward history has incorrect elements", Arrays.asList(place2, place3), navigationManager.getForwardHistory());

        // go forward

        navigationManager.goForward();
        assertEquals("Current place not as expected", place2, navigationManager.getCurrentPlace());
        assertEquals("Back history should have 1 places in it", 1, navigationManager.getBackHistory().size());
        assertEquals("Back history has incorrect elements", Arrays.asList(place1), navigationManager.getBackHistory());
        assertEquals("Forward history should have 1 place in it", 1, navigationManager.getForwardHistory().size());
        assertEquals("Forward history has incorrect elements", Arrays.asList(place3), navigationManager.getForwardHistory());

        // go forward (again)

        navigationManager.goForward();
        assertEquals("Current place not as expected", place3, navigationManager.getCurrentPlace());
        assertEquals("Back history should have 2 places in it", 2, navigationManager.getBackHistory().size());
        assertEquals("Back history has incorrect elements", Arrays.asList(place1, place2), navigationManager.getBackHistory());
        assertTrue("Forward history should be empty", navigationManager.getForwardHistory().size() == 0);

        // go back x 2 and then go to a new place (to check that forward history is cleared)

        Place place4 = new Place("place4");
        navigationManager.goBack();
        navigationManager.goBack();
        navigationManager.goTo(place4);
        assertEquals("Current place not as expected", place4, navigationManager.getCurrentPlace());
        assertEquals("Back history should have 1 place in it", 1, navigationManager.getBackHistory().size());
        assertEquals("Back history has incorrect elements", Arrays.asList(place1), navigationManager.getBackHistory());
        assertTrue("Forward history should be empty", navigationManager.getForwardHistory().size() == 0);
    }
}
