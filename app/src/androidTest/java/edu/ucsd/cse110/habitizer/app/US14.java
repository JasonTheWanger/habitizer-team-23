package edu.ucsd.cse110.habitizer.app;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.CoreMatchers.anyOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class US14 {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testScenario1_AsynchronousTimer() throws InterruptedException {
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.enter_routine_button))
                .perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.total_time_display))
                .check(matches(withText("0m")));

        Thread.sleep(3000);

        onView(withId(R.id.total_time_display))
                .check(matches(withText("0m")));

        Thread.sleep(2000);

        onView(withId(R.id.total_time_display))
                .check(matches(anyOf(
                        withText("0m"),
                        withText("1m")
                )));
    }

    @Test
    public void testScenario2_ManualAdvanceTime() {
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.enter_routine_button))
                .perform(click());

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
