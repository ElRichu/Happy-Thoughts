package h4213.smart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import h4213.smart.R;

public class LanguagesActivity extends AppCompatActivity {

    private Switch switchFR ;
    private Switch switchEN ;
    private Button next ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);
        switchEN = findViewById(R.id.switch_en);
        switchFR = findViewById(R.id.switch_fr);
        next = findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state_EN, state_FR ;

                if (!(switchEN.isChecked()) && !(switchFR.isChecked())){
                    Toast toast = Toast.makeText(getApplicationContext(),"Please select at least one language.",Toast.LENGTH_SHORT) ;
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
                    toast.show();
                }else {
                    if (switchEN.isChecked()){
                        state_EN = switchEN.getTextOn().toString();
                    }else{
                        state_EN = switchEN.getTextOff().toString();
                    }

                    if(switchFR.isChecked()){
                        state_FR = switchFR.getTextOn().toString();
                    }else{
                        state_FR = switchFR.getTextOff().toString();
                    }

                }

            //récuperer les données des languages sélectionnées ici
            }
        });
    }


}


