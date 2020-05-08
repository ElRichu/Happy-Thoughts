package h4213.smart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import h4213.smart.services.CategoriesExtractor;
import h4213.smart.services.DBHandler;
import h4213.smart.models.Interests;
import h4213.smart.R;
import h4213.smart.services.RecyclerViewAdapterCategory;

public class QuestionnaireActivity extends AppCompatActivity {

    private static final String TAG = "QuestionnaireActivity";

    private FirebaseAuth auth;
    private DBHandler dbHandler;
    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayList<ArrayList<String>> listSubCategories = new ArrayList<>();
    private RecyclerViewAdapterCategory adapter;
    private Interests myInterests;
    private FirebaseUser user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        dbHandler = new DBHandler();
        myInterests = Interests.getInstance();
        user = auth.getCurrentUser();

        setContentView(R.layout.activity_questionnaire);

        initButtonBack();
        initCategoryList();
    }

    private void initButtonBack(){
        Interests interet = Interests.getInstance();

        if(interet.getInterests().size() == 0){
            beGoneButtonBack();
        }
    }

    private void beGoneButtonBack(){
        Button t = findViewById(R.id.buttonBack);
        t.setVisibility(View.GONE);
    }

    private void initCategoryList(){

        BufferedReader reader = null;
        CategoriesExtractor extractor = new CategoriesExtractor();
        String jsonStr = "";

        try{
            reader = new BufferedReader( new InputStreamReader(getAssets().open("categories.json")));

            //get String
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                jsonStr += mLine;
            }

            //extract categories
            extractor.extract(jsonStr);

        } catch ( Exception e){
            Toast.makeText(this,"Problem", Toast.LENGTH_SHORT).show();
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        categoryNames = extractor.getCategoryNames();
        listSubCategories = extractor.getSubCategoryName();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategory);
        adapter = new RecyclerViewAdapterCategory(categoryNames, listSubCategories , this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void onBack(View v){
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
    }

    public void onFinish(View view){

        if(!myInterests.containInteret("other")){
            myInterests.addInterest("other");
        }
        ArrayList<String> interests = myInterests.getInterests();

        if(interests.size()<4){
            Toast.makeText(this, "Select at least 3 interests", Toast.LENGTH_SHORT).show();
        } else {

            // save interests list in firebase BD
            this.dbHandler.saveInterests(auth.getCurrentUser().getUid(), interests);

            //wipe
            Interests interestsHolder = Interests.getInstance();
            interestsHolder.clear();

            // redirect to HomePage
            Intent intent = new Intent(this, HomepageActivity.class);
            startActivity(intent);
        }
    }

}
