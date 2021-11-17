package com.vishav.barcode;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Before
    public void setUp()
    {
        ActivityScenario<LoginActivity> scenario=ActivityScenario.launch(LoginActivity.class);
        scenario.moveToState(Lifecycle.State.RESUMED);
    }

    @Test
    public void testInteraction()
    {
        Intents.init();
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("admin"));
        Espresso.onView(withId(R.id.password)).perform(ViewActions.typeText("admin"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.login)).perform(click());
        Intents.intended(hasComponent(MainActivity.class.getName()));
    }
}

