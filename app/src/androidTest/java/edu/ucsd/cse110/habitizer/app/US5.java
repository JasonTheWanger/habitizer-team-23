//package edu.ucsd.cse110.habitizer.app;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static androidx.test.espresso.action.ViewActions.replaceText;
//import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.CoreMatchers.allOf;
//import static org.hamcrest.Matchers.anyOf;
//import static org.hamcrest.Matchers.anything;
//
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.espresso.Espresso;
//import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.assertion.ViewAssertions;
//import androidx.test.espresso.matcher.ViewMatchers;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//public class US5 {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule =
//            new ActivityScenarioRule<>(MainActivity.class);
//
//
//    @Test
//    public void testMockElapsedTimeAndAdvanceTime() throws InterruptedException {
//        Espresso.onView(ViewMatchers.withText("Brush Teeth"))
//                .perform(ViewActions.click());
//
//        Thread.sleep(1000);
//
//        Espresso.onView(ViewMatchers.withText("Mock Mode"))
//                .perform(ViewActions.click());
//
//        Espresso.onView(ViewMatchers.withText("Advance"))
//                .perform(ViewActions.click());
//
//        onView(anyOf(
//                withText("00:31"),
//                withText("00:32"),
//                withText("00:33")
//        )).check(matches(isDisplayed()));
//    }
//
//
//    @Test
//    public void testRoutineEndsWithAllTasksCompleted() throws InterruptedException {
//        String[] tasks = {"Brush Teeth", "Make Bed", "Stretch", "Read a Book", "Plan the Day"};
//        for (String task : tasks) {
//            Espresso.onView(ViewMatchers.withText(task)).perform(ViewActions.click());
//            Thread.sleep(1000);
//            Espresso.onView(ViewMatchers.withText(task)).perform(ViewActions.click());
//        }
//        Espresso.onView(ViewMatchers.withText("ROUTINE ENDED"))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//
//        Espresso.onView(ViewMatchers.withId(R.id.total_time_display))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//    }
//}
