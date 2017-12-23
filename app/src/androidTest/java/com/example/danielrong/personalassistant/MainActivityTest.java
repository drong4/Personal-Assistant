package com.example.danielrong.personalassistant;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by danielrong on 11/14/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void startUp(){
        //Check if the mic button is there
        onView(withId(R.id.main_mic_btn)).check(matches(isDisplayed()));

        //Check if the toolbar is there
        onView(withId(R.id.main_toolbar)).check(matches(isDisplayed()));
    }
}
