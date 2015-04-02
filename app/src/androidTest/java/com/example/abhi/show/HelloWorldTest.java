package com.example.abhi.show;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;

/**
 * Created by abhi on 04/02/15.
 */
@LargeTest
public class HelloWorldTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public HelloWorldTest(){
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }
    public void testListGoesOverTheFold(){
        //OnView(withText("Hello World")).check(isDisplayed());
        onView(withId(R.id.briefText)).perform(typeText("Wazzup? how are you?"),closeSoftKeyboard());
        onView(withId(R.id.submitBtn)).perform(click());

        onView(withId(R.id.briefText)).check(matches(withText("Wazzup? how are you?")));
    }
}
