package h4213.smart;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import h4213.smart.activities.SettingsActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class TestSettingsActivity {

    @Rule
    public ActivityScenarioRule scenarioRule = new ActivityScenarioRule(SettingsActivity.class);

    @Test
    public void navHomePage() {
        onView(withId(R.id.buttonHomepage)).perform(click());
        onView(withId(R.id.HomePageActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void navHomePageButtonBack() {
        onView(withId(R.id.buttonBack)).perform(click());
        onView(withId(R.id.HomePageActivity)).check(matches(isDisplayed()));
    }
}
