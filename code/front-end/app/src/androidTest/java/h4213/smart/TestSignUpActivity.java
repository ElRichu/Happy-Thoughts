package h4213.smart;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import h4213.smart.activities.SignUpActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class TestSignUpActivity {

    @Rule
    public ActivityScenarioRule<SignUpActivity> scenarioRule = new ActivityScenarioRule(SignUpActivity.class);

    @Test
    public void enterEmail(){
        onView(withId(R.id.inputEmail)).perform(typeText("test@test.com"), closeSoftKeyboard());
    }

    @Test
    public void enterPassword(){
        onView(withId(R.id.inputPassword)).perform(typeText("123456"), closeSoftKeyboard());
    }

    @Test
    public void signUpSuccess() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = "ludo@test.com";
        onView(withId(R.id.inputEmail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.inputPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp_SignUp)).perform(click());

        //toast for sign up
        onView(withText("successfully create account."))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

        // redirect to homepage
        onView(withId(R.id.QuestionnaireActivity)).check(matches(isDisplayed()));

        //check if connected and delete account
        FirebaseUser user = auth.getCurrentUser();
        Assert.assertNotNull(user);

        user.delete();
    }

    @Test
    public void signUpFailAlreadyExist(){
        String email = "test@test.com";
        onView(withId(R.id.inputEmail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.inputPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp_SignUp)).perform(click());

        //toast for sign up
        onView(withText("Account creation failed."))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

    }

    @Test
    public void SignUpInvalidFormat(){
        onView(withId(R.id.inputEmail)).perform(typeText("wrong-FormatOn.Purpose"), closeSoftKeyboard());
        onView(withId(R.id.inputPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp_SignUp)).perform(click());

        //toast for sign up
        onView(withText("Invalid format, please try again."))
                .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void navAuthenticationActivity(){
        onView(withId(R.id.buttonBack)).perform(click());
        onView(withId(R.id.authenticationActivity)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}