package h4213.smart;

import org.junit.Before;

import h4213.smart.activities.SignUpActivity;

import static org.junit.Assert.assertEquals;

public class UnitTestSignUp {

    private SignUpActivity signUpActivity;

    @Before
    public void init(){
        signUpActivity = new SignUpActivity();
    }

    // je peux pas le tester car la méthode est privée
    /*@Test
    public void testEmail(){
        String email = "test-mail_regex@jesais-pas.fr";
        Assert.assertTrue(signUpActivity.ver);

    }*/


}
