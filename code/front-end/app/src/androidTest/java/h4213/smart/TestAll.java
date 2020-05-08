package h4213.smart;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {TestSignUpActivity.class,
        TestAuthenticationActivity.class,
        TestSettingsActivity.class,
        TestHomepageActivity.class,
        TestScenario.class}
)
public class TestAll {
}
