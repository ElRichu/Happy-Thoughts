package h4213.smart.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import h4213.smart.services.DBHandler;
import h4213.smart.models.Interests;
import h4213.smart.models.MyCallBack;
import h4213.smart.R;
import h4213.smart.services.RecyclerViewAdapterTweet;
import h4213.smart.services.TweetExtractor;
import h4213.smart.models.Tweets;

public class HomepageActivity extends AppCompatActivity {

    private static final String TAG = "HomepageActivity";
    private FirebaseAuth auth ;
    private FirebaseUser user ;
    private DBHandler dbHandler;
    private RecyclerViewAdapterTweet adapter;
    private  RecyclerView recyclerViewTweets;
    private TextToSpeech tts;
    private boolean readTweets;

    private String userName;
    private ArrayList<Tweets> listTweet;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        auth = FirebaseAuth.getInstance() ;
        user = auth.getCurrentUser();
        dbHandler = new DBHandler();
        userName = "";
        listTweet = new ArrayList<>();

        TextView nameV = findViewById(R.id.nameUser);

        try {
            if (getIntent().getExtras().getBoolean("readTweets")) {
                this.readTweets = true;
                this.tts = new TextToSpeech(getApplicationContext(),status->{});
                this.tts.setLanguage(Locale.ENGLISH);
            }
        }catch(NullPointerException e){
            Log.w(TAG,"TextToSpeech - say the tweets out loud is impossible");
        }


        String name = "Here's some good news, \n " ;
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                if (profile.getProviderId().equals(TwitterAuthProvider.PROVIDER_ID)) {
                    // UID specific to the provider
                    String uid = profile.getUid();

                    // Name, email address, and profile photo Url
                    String n = profile.getDisplayName();
                    n.replaceAll("[\\t\\n\\r]+"," ");
                    name += n ;
                    String email = profile.getEmail();
                    //Uri photoUrl = profile.getPhotoUrl();

                    Log.w("DEBUG", String.format("uid=%s name=%s email=%s",
                            uid, name, email));
                } else if (profile.getProviderId().equals(EmailAuthProvider.PROVIDER_ID)){

                    dbHandler.getUserNameFromDB(user.getUid(), new MyCallBack(

                    ) {
                        @Override
                        public void onCallBack(ArrayList<String> list) {
                            userName = list.get(0);
                            String name = "Here's some good news, \n " + userName + " !";
                            nameV.setText(name);
                        }
                    });

                }else if (profile.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID)){
                    String n = user.getEmail();
                    n.replaceAll("[@gmail.com]+","");
                    name +=n;
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

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

        name += " !";
        nameV.setText(name);

        dbHandler.getInterests(user.getUid(), new MyCallBack() {
            @Override
            public void onCallBack(ArrayList<String> listValue) {

                JSONArray jsonInterestsList = new JSONArray(listValue);
                // add otherCategory?

                // reinit interests to preselect
                Interests interests = Interests.getInstance();
                interests.clear();
                try{
                    for(int i = 0 ; i < jsonInterestsList.length() ; i++){
                        interests.addInterest((String) jsonInterestsList.get(i));
                    }
                } catch (Exception e) {
                    // Init interests not possible - Display error ?
                    Log.w(TAG, "Can't initialize the interests");
                }

                String jsonForRequest = "{\n" +
                        "\"interet\":   " + jsonInterestsList.toString() +"\n" +
                        "}";

                try{
                    requestAPI(new JSONObject(jsonForRequest));
                } catch (Exception e){
                    Log.w(TAG, "Can't create object for request -- can't request");
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void readTweets(){
        if(this.tts!=null) {
            String toSay = "";
            for (Tweets tweet : listTweet) {
                toSay += tweet.toSay();
            }
            String regexUrl = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            toSay=toSay.replaceAll(regexUrl,"");
            Log.w(TAG, "TextToSpeech - " + toSay);
            tts.speak(toSay, TextToSpeech.QUEUE_FLUSH, null, "tweetsSpeech");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void requestAPI(JSONObject jsonForReq){

        String url = getString(R.string.tweets_server_address);
        url = url.replace("http:", "https:");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonForReq,
                response -> {
                    initDisplayTweets(response.toString());
                },
                error -> {
                    Log.w(TAG, "Error - server down?");
                    String errorMessage = error.getMessage();
                    if(errorMessage != null){
                        Log.w(TAG, error.getMessage() );
                    }
                    Toast.makeText(this, "Error getting tweets, retry later", Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(objectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initDisplayTweets(String jsonExample){
        TweetExtractor tweetExtractor = new TweetExtractor();
        try{
            tweetExtractor.extract(jsonExample);
        } catch(Exception e){
            Log.w(TAG, "Error initializing tweets");
        }

        listTweet = tweetExtractor.getListTweet();

        recyclerViewTweets = findViewById(R.id.recyclerViewTweets);
        adapter = new RecyclerViewAdapterTweet(listTweet , this);
        recyclerViewTweets.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTweets.setAdapter(adapter);
        readTweets();
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToAlarm(View view) {
        Intent intent = new Intent(this, AlarmSettingActivity.class);
        startActivity(intent);
        finish();
    }
    public void onDestroy(){
        super.onDestroy();
        if(tts!=null){
            tts.shutdown();
        }
    }

    public void onEntertainment(View view){
        ImageButton button = findViewById(R.id.buttonCategoryComedy);
        resetFilter();
        button.setBackground(getResources().getDrawable(R.drawable.entertainment_icon_grey));
        onFilter("entertainment");
    }

    public void onSportFilter(View view) {
        ImageButton button = findViewById(R.id.buttonCategorySports);
        resetFilter();
        button.setBackground(getResources().getDrawable(R.drawable.sports_icon_grey));
        onFilter("sports");
    }

    public void onNoFilter(View view) {
        resetFilter();
        onFilter("all");
    }

    public void onOtherFilter(View view){
        ImageButton button = findViewById(R.id.buttonCategoryOthers);
        resetFilter();
        button.setBackground(getResources().getDrawable(R.drawable.others_icon_grey));
        onFilter("other");
    }

    public void resetFilter(){
        ImageButton button = findViewById(R.id.buttonCategoryComedy);
        button.setBackground(getResources().getDrawable(R.drawable.entertainment_icon_white));
        button = findViewById(R.id.buttonCategorySports);
        button.setBackground(getResources().getDrawable(R.drawable.sports_icon_white));
        button = findViewById(R.id.buttonCategoryOthers);
        button.setBackground(getResources().getDrawable(R.drawable.others_icon_white));
    }

    public void onFilter(String category){

        ArrayList<Tweets> filteredList = new ArrayList<>();

        if(category.equals("all")){
            filteredList = listTweet;
        } else {
            for(Tweets tweets: listTweet){
                if (tweets.getCategory().equals(category)){
                    filteredList.add(tweets);
                }
            }
        }

        try{
            adapter = new RecyclerViewAdapterTweet(filteredList , this);
            recyclerViewTweets.setAdapter(adapter);
        } catch (Exception e){
            Toast.makeText(this, "Error loading tweets - retry later", Toast.LENGTH_SHORT).show();
        }


    }

}