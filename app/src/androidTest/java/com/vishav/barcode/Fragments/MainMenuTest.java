package com.vishav.barcode.Fragments;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.vishav.barcode.LoginActivity;
import com.vishav.barcode.MainActivity;
import com.vishav.barcode.R;
import com.vishav.barcode.bulkAdd;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainMenuTest extends TestCase {

    @Before
    public void setUp()
    {
        ActivityScenario<MainActivity> scenario=ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
    }

    @Test
    public void userAlreadyLoggedIn()
    {
        Intents.init();
        Espresso.onView(withText("Add Bulk Tickets"))
                .perform(click());
        Intents.intended(hasComponent(LoginActivity.class.getName()),Intents.times(0));
    }

    @Test
    public void syncDb()
    {
        Espresso.onView(withText("Checkings"))
                .perform(click());
        Espresso.onView(withText("Morrison's")).check(matches(isDisplayed()));
    }
}