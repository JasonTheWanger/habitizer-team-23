package edu.ucsd.cse110.habitizer.app;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
public class US3c {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testMockElapsedTimeAndAdvanceTime() throws InterruptedException {
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.enter_routine_button))
                .perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.total_time_display))
                .check(matches(withText("0m")));

        onView(withId(R.id.MockModeButton)).perform(click());

        onView(withId(R.id.MockModeButton)).perform(click());
        onView(withId(R.id.MockModeButton)).perform(click());

        onView(withId(R.id.total_time_display))
                .check(matches(withText("0m")));

        onView(withId(R.id.MockModeButton)).perform(click());
        onView(withId(R.id.MockModeButton)).perform(click());

        onView(withId(R.id.total_time_display))
                .check(matches(withText("1m")));
    }
}


