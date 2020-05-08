package h4213.smart;

import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Rule;
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

public class TestAuthenticationActivity {

    @Rule
    public ActivityScenarioRule<AuthenticationActivity> scenarioRule = new ActivityScenarioRule(AuthenticationActivity.class);

    @Test
    public void testEmail(){
        onView(withId(R.id.inputEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.inputEmail)).perform(typeText("test@test.com"), closeSoftKeyboard());
    }

    @Test
    public void testPassword(){
        onView(withId(R.id.inputPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.inputPassword)).perform(typeText("123456"), closeSoftKeyboard());
    }

    @Test
    public void testLoginButtonSuccess(){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.inputEmail)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.inputPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        FirebaseUser user = auth.getCurrentUser();
        assert(user != null);

        //test if toast is displayed
        onView(withText("Authentication successful."))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

        //tester navigation in activity
        onView(withId(R.id.HomePageActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginButtonFail(){
        onView(withId(R.id.inputEmail)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.inputPassword)).perform(typeText("wrongpwdtofaillogin"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        //test if toast is displayed
        onView(withText("Authentication failed."))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginInvalidFormatEmail(){
        onView(withId(R.id.inputEmail)).perform(typeText("test.com"), closeSoftKeyboard()); //wrong format on purpose
        onView(withId(R.id.inputPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());

        //test if toast is displayed
        onView(withText("Invalid format, please try again."))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSignUpNav(){
        onView(withId(R.id.buttonSignUp_Authentication)).perform(click());
        onView(withId(R.id.SignUpActivity)).check(matches(isDisplayed()));
    }

    @Test
    public void testForgotPassword(){
        onView(withId(R.id.buttonForgottenPwd)).perform(click());
        //tester quand le code sera op ^^
    }

    @Test
    public void testPopUpTwitter(){
        onView(withId(R.id.buttonLogInTwitter)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonLogInTwitter)).perform(click());

        onView(withId(R.id.TwitterLoginActivity))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));

    }

    @Test
    public void testPopUpGoogle(){
        onView(withId(R.id.buttonLogInGoogle)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonLogInGoogle)).perform(click());

        onView(withId(R.id.GoogleLoginActivty))
                .check(matches(isDisplayed()));
    }

}
