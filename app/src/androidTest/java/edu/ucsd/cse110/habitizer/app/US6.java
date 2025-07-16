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
//
//@RunWith(AndroidJUnit4.class)
//public class US6 {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule =
//            new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void testElapsedTimeForEachTask() throws InterruptedException {
//        Espresso.onView(ViewMatchers.withText("Brush Teeth"))
//                .perform(ViewActions.click());
//
//        Thread.sleep(3000);
//
//        Espresso.onView(ViewMatchers.withText("Advance"))
//                .perform(ViewActions.click());
//
//        Espresso.onView(ViewMatchers.withText("Brush Teeth"))
//                .perform(ViewActions.click());
//
//        Espresso.onView(ViewMatchers.withText("1m"))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//    }
//
//}