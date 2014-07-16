package com.zenjava.jfxflow.navigation;

import com.google.common.truth.Expect;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TestDefaultNavigationManager
{

    @Rule
    public final Expect expect = Expect.create();

    @Test
    public void testNavigation()
    {
        DefaultNavigationManager navigationManager = new DefaultNavigationManager();

        expect.that(navigationManager.getCurrentPlace()).named("Current place at start").isNull();
        expect.that(navigationManager.getBackHistory()).named("Back history at start").isEmpty();
        expect.that(navigationManager.getForwardHistory()).named("Forward history at start").isEmpty();

        // add place1

        Place place1 = new Place("place1");
        navigationManager.goTo(place1);
        expect.that(navigationManager.getCurrentPlace()).named("Current place").isEqualTo(place1);
        expect.that(navigationManager.getBackHistory()).named("Back history").isEmpty();
        expect.that(navigationManager.getForwardHistory()).named("Forward history").isEmpty();

        // add place2

        Place place2 = new Place("place2");
        navigationManager.goTo(place2);
        expect.that(navigationManager.getCurrentPlace()).named("Current place").isEqualTo(place2);
        expect.that(navigationManager.getBackHistory()).named("Back history").containsSequence(Arrays.asList(place1));
        expect.that(navigationManager.getForwardHistory()).named("Forward history").isEmpty();

        // add place3

        Place place3 = new Place("place3");
        navigationManager.goTo(place3);
        expect.that(navigationManager.getCurrentPlace()).named("Current place").isEqualTo(place3);
        expect.that(navigationManager.getBackHistory()).named("Back history").containsSequence(Arrays.asList(place1, place2));
        expect.that(navigationManager.getForwardHistory()).named("Forward history").isEmpty();

        // go back

        navigationManager.goBack();
        expect.that(navigationManager.getCurrentPlace()).named("Current place").isEqualTo(place2);
        expect.that(navigationManager.getBackHistory()).named("Back history").containsSequence(Arrays.asList(place1));
        expect.that(navigationManager.getForwardHistory()).named("Forward history").containsSequence(Arrays.asList(place3));

        // go back (again)

        navigationManager.goBack();
        expect.that(navigationManager.getCurrentPlace()).named("Current place").isEqualTo(place1);
        expect.that(navigationManager.getBackHistory()).named("Back history").isEmpty();
        expect.that(navigationManager.getForwardHistory()).named("Forward history").containsSequence(Arrays.asList(place2, place3));

        // go forward

        navigationManager.goForward();
        expect.that(navigationManager.getCurrentPlace()).named("Current place").isEqualTo(place2);
        expect.that(navigationManager.getBackHistory()).named("Back history").containsSequence(Arrays.asList(place1));
        expect.that(navigationManager.getForwardHistory()).named("Forward history").containsSequence(Arrays.asList(place3));

        // go forward (again)

        navigationManager.goForward();
        expect.that(navigationManager.getCurrentPlace()).named("Current place").isEqualTo(place3);
        expect.that(navigationManager.getBackHistory()).named("Back history").containsSequence(Arrays.asList(place1, place2));
        expect.that(navigationManager.getForwardHistory()).named("Forward history").isEmpty();

        // go back x 2 and then go to a new place (to check that forward history is cleared)

        Place place4 = new Place("place4");
        navigationManager.goBack();
        navigationManager.goBack();
        navigationManager.goTo(place4);
        expect.that(navigationManager.getCurrentPlace()).named("Current place").isEqualTo(place4);
        expect.that(navigationManager.getBackHistory()).named("Back history").containsSequence(Arrays.asList(place1));
        expect.that(navigationManager.getForwardHistory()).named("Forward history").isEmpty();
    }
}
