package h4213.smart;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

import h4213.smart.activities.HomepageActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class TestHomepageActivity {

    @Rule
    public ActivityScenarioRule<HomepageActivity> scenarioRule = new ActivityScenarioRule(HomepageActivity.class);

    @Test
    public void navSettings() {
        onView(withId(R.id.buttonCategorySettings)).perform(click());
        onView(withId(R.id.activitySettings)).check(matches(isDisplayed()));
    }

    // add tests to display tweets and test filters
}
