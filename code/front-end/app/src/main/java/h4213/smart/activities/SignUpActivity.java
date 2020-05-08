package h4213.smart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import h4213.smart.services.DBHandler;
import h4213.smart.services.GoogleLogin;
import h4213.smart.models.Interests;
import h4213.smart.R;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DBHandler dbHandler;

    private static final String TAG = "SignUpActivity";

    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        dbHandler = new DBHandler();

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputName = findViewById(R.id.inputName);

        Button buttonTwitter = findViewById(R.id.buttonLogInTwitter);
        buttonTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                signInTwitter(v);
            }
        });

        //login via twitter via first button
        Button popupButtonGoogle1 = findViewById(R.id.buttonLogInGoogle);
        popupButtonGoogle1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signInGoogle(v);
            }
        });

        //login via twitter via second button
        ImageButton popupButtonTwitter2 = findViewById(R.id.imageButtonTwitter);
        popupButtonTwitter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               signInTwitter(v);
            }
        });

        //login via twitter via second button
        ImageButton popupButtonGoogle2 = findViewById(R.id.imageButtonGoogle);
        popupButtonGoogle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleLogin popUpClassGoogle = new GoogleLogin();
                popUpClassGoogle.showPopupWindow(v);
            }
        });
    }


    public void onClickBack(View view) {
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }

    public void signUp(String email, String password,final String name){
        Log.d(TAG, "createAccount:" + email);
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(SignUpActivity.this, "successfully create account.",
                                Toast.LENGTH_SHORT).show();
                        FirebaseUser user = auth.getCurrentUser();

                        // save name to database
                        dbHandler.saveUserName(user.getUid(), name);

                        //clear all interests (previous use)
                        Interests interests = Interests.getInstance();
                        interests.clear();

                        // route l'utilisateur sur home page
                        nextUI();
                    } else {
                        // If sign up fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Account creation failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });

    }

    private boolean validateForm(String email, String password,String name){

        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Field required.");
            valid = false;
        } else if(!emailValid(email)) {
            inputEmail.setError("Invalid format.");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Field required.");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        if (TextUtils.isEmpty(name)) {
            inputName.setError("Field required.");
            valid = false;
        } else {
            inputName.setError(null);
        }

        return valid;
    }

    private boolean emailValid(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w(-?\\w)?]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private void nextUI(){
        startActivity(new Intent(this, QuestionnaireActivity.class));
        this.finish();
    }



    public void onSignUp(View view) {
        String email = inputEmail.getText().toString();
        String pwd = inputPassword.getText().toString();
        String name = inputName.getText().toString();

        if (!validateForm(email, pwd,name)) {
            Toast.makeText(getApplicationContext(), "Invalid format, please try again.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        signUp(email,pwd,name);
    }

    public void signInTwitter(View v){

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

        Task<AuthResult> pendingResultTask = auth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    nextUI();
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
            auth
                    .startActivityForSignInWithProvider( this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    authResult.getAdditionalUserInfo().getProfile();
                                    nextUI();
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken();
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"error : "+ e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
        }
    }

    public void signInGoogle(View v){

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("google.com");

        Task<AuthResult> pendingResultTask = auth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    nextUI();
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
            auth
                    .startActivityForSignInWithProvider( this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    authResult.getAdditionalUserInfo().getProfile();
                                    nextUI();
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken();
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"error : "+ e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
        }
    }

}