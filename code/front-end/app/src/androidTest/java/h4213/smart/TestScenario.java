package h4213.smart;

import androidx.test.core.app.ActivityScenario;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Assert;
import org.junit.Test;

import h4213.smart.activities.AuthenticationActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class TestScenario {

    @Test
    public void testSignInOutEmail(){
        ActivityScenario scenario = ActivityScenario.launch(AuthenticationActivity.class);

        // first we connect the user
        FirebaseAuth auth = FirebaseAuth.getInstance();

        onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.inputEmail)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.inputPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        FirebaseUser user = auth.getCurrentUser();
        Assert.assertNotNull(user);

        //test if toast is displayed
        onView(withText("Authentication successful."))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

        //tester navigation in activity
        onView(withId(R.id.HomePageActivity)).check(matches(isDisplayed()));


        // nav to settings
        onView(withId(R.id.buttonCategorySettings)).perform(click());
        onView(withId(R.id.activitySettings)).check(matches(isDisplayed()));

        // disconnect user
        onView(withId(R.id.buttonLogOut)).perform(click());
        onView(withId(R.id.authenticationActivity)).check(matches(isDisplayed()));

        user = auth.getCurrentUser();
        Assert.assertNull(user);
    }
}
