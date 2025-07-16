package edu.ucsd.cse110.habitizer.app;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class US18 {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testRoutineStatePersistence() throws InterruptedException {

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.enter_routine_button))
                .perform(click());

        Thread.sleep(1000);

        onData(anything()).inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.task_timer))
                .check(matches(withText("0secs")));

        onData(anything()).inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.checkview))
                .perform(click());

        Thread.sleep(2000);

        onData(anything()).inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.checkview))
                .perform(click());

        activityRule.getScenario().close();
        ActivityScenario.launch(MainActivity.class);

        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.enter_routine_button))
                .perform(click());

        Thread.sleep(1000);

        onData(anything()).inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.task_timer))
                .check(matches(withText("5secs")));

    }
}
