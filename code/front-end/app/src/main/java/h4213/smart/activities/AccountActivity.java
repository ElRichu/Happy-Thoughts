package h4213.smart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;

import h4213.smart.services.DBHandler;
import h4213.smart.models.MyCallBack;
import h4213.smart.R;

public class AccountActivity extends AppCompatActivity {

    private TextView Email ;
    private TextView Name ;
    private TextView Phone ;
    private TextView Method ;
    private DBHandler dbHandler ;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private static final String TAG = "AccountActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbHandler = new DBHandler();

        Email = findViewById(R.id.email);
        Name = findViewById(R.id.name);
        Phone = findViewById(R.id.phone);
        Method = findViewById(R.id.method);

        if (user != null) {
            String name = "";
            String email = "";
            String phone = "";

            for (UserInfo profile : user.getProviderData()) {
                if (profile.getProviderId().equals(TwitterAuthProvider.PROVIDER_ID)) {
                    String method = "twitter authentication";
                    // UID specific to the provider
                    String uid = profile.getUid();

                    // Name, email address, and profile photo Url
                    String n = profile.getDisplayName();
                    n.replaceAll("[\\t\\n\\r]+"," ");
                    name += n ;
                    email += profile.getEmail();
                    phone += profile.getPhoneNumber();

                    //Uri photoUrl = profile.getPhotoUrl();
                    Email.setText(email);
                    Phone.setText(phone);
                    Name.setText(name);
                    Method.setText(method);
                    if (email=="" || email== null) {
                        TextView t = findViewById(R.id.textViewEmail);
                        t.setVisibility(View.GONE);
                        Email.setVisibility(View.GONE);

                    }
                    if (phone=="" || phone==null) {
                        TextView t = findViewById(R.id.textViewPhone);
                        t.setVisibility(View.GONE);
                        Phone.setVisibility(View.GONE);
                    }

                    Log.w("DEBUG", String.format("uid=%s name=%s email=%s",
                            uid, name, email));
                } else if (profile.getProviderId().equals(EmailAuthProvider.PROVIDER_ID)){
                    //TO DO : store name in firebase database and access it here to display it
                    String method = "email authentication";
                    Method.setText(method);

                    email = user.getEmail();
                    Email.setText(email);

                    TextView t = findViewById(R.id.textViewPhone);
                    t.setVisibility(View.GONE);
                    Phone.setVisibility(View.GONE);

                    dbHandler.getUserNameFromDB(user.getUid(), new MyCallBack(

                    ) {
                        @Override
                        public void onCallBack(ArrayList<String> list) {
                            String name = list.get(0);
                            Name.setText(name);
                        }
                    });

                }else if (profile.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID)){
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

                    String method = "google authentication";
                    Method.setText(method);

                    email = user.getEmail();
                    Email.setText(email);

                    TextView t = findViewById(R.id.textViewPhone);
                    t.setVisibility(View.GONE);
                    Phone.setVisibility(View.GONE);

                    TextView t2 = findViewById(R.id.textViewName);
                    t2.setVisibility(View.GONE);
                    Name.setVisibility(View.GONE);



                    if (acct != null) {
                        String personName = acct.getDisplayName();
                        String personGivenName = acct.getGivenName();
                        String personFamilyName = acct.getFamilyName();
                        String personEmail = acct.getEmail();
                        String personId = acct.getId();
                        Uri personPhoto = acct.getPhotoUrl();
                        name+=personName;
                    }
                }
            }
        }



    }

    public void goToHome(View view){
        Intent intent = new Intent(this, HomepageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK) ;
        startActivity(intent);
        finish();
    }

    public void goBack(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK) ;
        startActivity(intent);
        finish();
    }
}
