package h4213.smart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import h4213.smart.models.Interests;
import h4213.smart.R;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private static final String TAG = "SettingsActivity";
    private String name;
    private String email;
    private Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
             name = user.getDisplayName();
             email = user.getEmail();
             photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
        } else {
            Toast.makeText(SettingsActivity.this, "You have been logged out.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            this.finish();
        }

        //CHANGE CREDENTIALS
        ImageButton changeCredentials = findViewById(R.id.buttonChangePwd);
        changeCredentials.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CredentialsSettingsActivity popUpClass = new CredentialsSettingsActivity();
                popUpClass.showPopupWindow(v);
            }
        });

        Button changeCredentials2 = findViewById(R.id.buttonCredentials);
        changeCredentials2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                CredentialsSettingsActivity popUpClass = new CredentialsSettingsActivity();
                popUpClass.showPopupWindow(v);
            }
        });

        //CHANGE PREFERENCES
        ImageButton changePreferences = findViewById(R.id.buttonPreferences);
        changePreferences.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // redirect to QuestionnaryPage
                nextUI();
            }
        });

        Button changePreferences2 = findViewById(R.id.buttonQuestionnary);
        changePreferences2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // redirect to QuestionnaryPage
                nextUI();
            }
        });

        //login via twitter via first button
        ImageButton popup1 = findViewById(R.id.imageArrow4);
        popup1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                HelpPopUpActivity popUpClass = new HelpPopUpActivity();
                popUpClass.showPopUpWindow(v);
            }
        });

    }

    public void onLogOut(View view) {
        Interests interests = Interests.getInstance();
        interests.clear();
        auth.signOut();
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
        this.finish();
    }


    public void goToHome(View view){
        Intent intent = new Intent(this, HomepageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK) ;
        startActivity(intent);
        finish();
    }

    public void goToAccount(View view){
        Intent intent = new Intent(this, AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK) ;
        startActivity(intent);
        finish();
    }

    private void nextUI() {

        startActivity(new Intent(this, QuestionnaireActivity.class));
        this.finish();
    }
}
