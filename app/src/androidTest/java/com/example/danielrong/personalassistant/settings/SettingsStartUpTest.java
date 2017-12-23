package com.example.danielrong.personalassistant.settings;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.danielrong.personalassistant.MainActivity;
import com.example.danielrong.personalassistant.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by danielrong on 11/14/17.
 */

@RunWith(AndroidJUnit4.class)
public class SettingsStartUpTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickToSettings(){
        goToSettings();
    }

    @Test
    public void checkSettingsFields(){
        goToSettings();

        //Check if fields exist
        onView(withText("Attentive Mode")).check(matches(isDisplayed()));
        onView(withText("Wake-up Phrase")).check(matches(isDisplayed()));
    }

    private void goToSettings(){
        //Open up the menu in the actionbar
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        //Click on the settings button
        onView(withText("Settings")).perform(click());
    }
}
