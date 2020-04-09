package com.mehul.redditwall;

import android.content.Context;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.mehul.redditwall.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UiTests {
    @Rule
    public ActivityTestRule mainRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void menuTest() {
        String[] menuItems = {"Saved Subs", "History", "Recommendations", "Settings"};
        onView(withId(R.id.search)).perform(click());
        onView(withId(R.id.fav_pics)).perform(click());
        onView(isRoot()).perform(pressBack());
        for (String menuItem : menuItems) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            onView(withText(menuItem)).perform(click());

            if (!menuItem.equals("Settings")) {
                onView(isRoot()).perform(pressBack());
            }
        }
        onView(withId(R.id.dark_switch)).perform(scrollTo(), click());
        onView(withId(R.id.dark_switch)).perform(scrollTo(), click());
        onView(isRoot()).perform(pressBack());
    }

    @Test
    public void favsTest() {
        onView(withId(R.id.fav_pics)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <= 90; i++) {
            onView(withId(R.id.fav_scroll)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
            onView(withId(R.id.load_post)).perform(click());
            onView(isRoot()).perform(pressBack());
            onView(isRoot()).perform(pressBack());
        }
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();

        assertEquals("com.mehul.redditwall", appContext.getPackageName());
    }
}
