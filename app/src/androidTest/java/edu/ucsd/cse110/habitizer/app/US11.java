package edu.ucsd.cse110.habitizer.app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class US11 {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAddCustomRoutine() throws InterruptedException {
        onView(withId(R.id.AddRoutineButton)).perform(click());
        onView(withId(R.id.new_routine_name))
                .perform(typeText("Study Routine"), closeSoftKeyboard());
        onView(withText("Add")).perform(click());

        onView(withText("Study Routine")).check(matches(isDisplayed()));
    }
}
