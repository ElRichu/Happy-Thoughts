package h4213.smart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import h4213.smart.models.Interests;
import h4213.smart.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Interests interests = Interests.getInstance();
        interests.clear();
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Il faudra vérifier que y a un utilisateur connecté
        Intent intent = new Intent(this, AuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent); //permet de naviguer vers l'activité de connexion
        this.finish();
    }
}
