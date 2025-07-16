package edu.ucsd.cse110.habitizer.app;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class US12 {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testTaskReordering() {
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.enter_routine_button))
                .perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.position_down_buttom))
                .perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(1)
                .onChildView(withId(R.id.position_up_buttom))
                .perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.task_name))
                .check(matches(withText("Brush Teeth")));

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(1)
                .onChildView(withId(R.id.task_name))
                .check(matches(withText("Make Bed")));

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.position_down_buttom))
                .perform(click());

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.task_name))
                .check(matches(withText("Make Bed")));

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(1)
                .onChildView(withId(R.id.task_name))
                .check(matches(withText("Brush Teeth")));
    }
}
