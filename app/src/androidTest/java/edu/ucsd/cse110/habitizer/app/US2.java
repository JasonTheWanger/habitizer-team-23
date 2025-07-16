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
//
//public class US2 {
//
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityRule =
//            new ActivityScenarioRule<>(MainActivity.class);
//
//    @Test
//    public void testRoutineNameIsMorningRoutine() {
//        onView(withId(R.id.routine_name))
//                .check(matches(withText("Morning Routine")));
//
//    }
//
//        @Test
//        public void testClickBrushTeethTwiceAndCheckComplete() throws InterruptedException {
//            Espresso.onView(ViewMatchers.withText("Brush Teeth"))
//                    .perform(ViewActions.click())
//                    .perform(ViewActions.click());
//
//            Espresso.onView(ViewMatchers.withText("[Complete]"))
//                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//
//            Thread.sleep(1000);
//        }
//}
