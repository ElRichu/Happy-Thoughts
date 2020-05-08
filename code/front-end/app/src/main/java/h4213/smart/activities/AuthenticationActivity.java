package h4213.smart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import h4213.smart.R;
import h4213.smart.models.Interests;

public class AuthenticationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private static final String TAG = "AuthenticationActivity";

    private EditText inputEmail ;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        auth = FirebaseAuth.getInstance();

        //clear interests
        Interests interests = Interests.getInstance();
        interests.clear();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, HomepageActivity.class));
            finish();
        }

        //find Views
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        inputEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputEmail.getCurrentTextColor()!=getResources().getColor(R.color.greyBar)) {
                    inputEmail.setTextColor(getResources().getColor(R.color.greyBar));
                    inputPassword.setTextColor(getResources().getColor(R.color.greyBar));
                }
                if (inputPassword.getCurrentTextColor()!=getResources().getColor(R.color.greyBar)) {
                    inputPassword.setTextColor(getResources().getColor(R.color.greyBar));
                    inputEmail.setTextColor(getResources().getColor(R.color.greyBar));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        inputPassword.addTextChangedListener(new TextWatcher() {

            //changement de couleur du texte input si il y avait erreur avant
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputEmail.getCurrentTextColor()!=getResources().getColor(R.color.greyBar)) {
                    inputEmail.setTextColor(getResources().getColor(R.color.greyBar));
                    inputPassword.setTextColor(getResources().getColor(R.color.greyBar));
                }
                if (inputPassword.getCurrentTextColor()!=getResources().getColor(R.color.greyBar)) {
                    inputPassword.setTextColor(getResources().getColor(R.color.greyBar));
                    inputEmail.setTextColor(getResources().getColor(R.color.greyBar));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button buttonTwitter = findViewById(R.id.buttonLogInTwitter);
        buttonTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                signInTwitter(v);
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


        Button popupButtonGoogle1 = findViewById(R.id.buttonLogInGoogle);
        popupButtonGoogle1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signInGoogle(v);
            }
        });


        ImageButton popupButtonGoogle2 = findViewById(R.id.imageButtonGoogle);
        popupButtonGoogle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInGoogle(v);
            }
        });

    }

    public void onSignUpClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onForgottenPwdClick(View view){
        String email = inputEmail.getText().toString();
        if (!emailValid(email)) {
            Toast.makeText(getApplicationContext(), "Invalid format, please try again.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "An email has been sent. Please reset your password. ",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Your email is not registered with us. Please note that if you " +
                                            "had used Google or Twitter to sign up, it is not possible to reset your password here.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private boolean emailValid(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w(-?\\w)?]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    private boolean validateForm(String email, String password){

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
            inputPassword.setError("Required.");
            valid = false;
        } else {
            inputPassword.setError(null);
        }


        return valid;
    }
    public void onSignIn(View view){
        String email = inputEmail.getText().toString();
        String pwd = inputPassword.getText().toString();
        if (!validateForm(email, pwd)) {
            Toast.makeText(getApplicationContext(), "Invalid format, please try again.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        signIn(email,pwd);
    }

    private void nextUI() {

        startActivity(new Intent(this, HomepageActivity.class));
        this.finish();
    }

    public void signIn(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
       .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            // On route l'utilisateur vers page accueil
                            Toast.makeText(getApplicationContext(),"Authentication successful.",Toast.LENGTH_SHORT).show();
                            nextUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            inputPassword.setTextColor(getResources().getColor(R.color.fakeBloodRed));
                            inputEmail.setTextColor(getResources().getColor(R.color.fakeBloodRed));
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                                    authResult.getAdditionalUserInfo().getProfile();
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
                                    authResult.getAdditionalUserInfo().getProfile();
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
                    .startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    authResult.getAdditionalUserInfo().getProfile();
                                    nextUI();
                                    // The OAuth access token can also be retrieved:
                                    //((OAuthCredential) authResult).getAccessToken();
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "error : " + e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
        }

        // Build a GoogleSignInClient with the options specified by gso.
        //GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);




    }

}