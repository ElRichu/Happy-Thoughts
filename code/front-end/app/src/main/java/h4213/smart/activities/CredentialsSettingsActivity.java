package h4213.smart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import h4213.smart.R;

public class CredentialsSettingsActivity extends AppCompatActivity {

    private FirebaseAuth auth ;
    private FirebaseUser user ;

    public void showPopupWindow(final View view) {
        //extrait de code source : https://medium.com/@evanbishop/popupwindow-in-android-tutorial-6e5a18f49cc7
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_credentials_settings, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //if outside zone of pop-up is clicked
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });

        Button buttonYes = popupView.findViewById(R.id.buttonYes);
        buttonYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user!=null) {
                    String email = user.getEmail();
                    auth.sendPasswordResetEmail(email);
                    Toast.makeText(v.getContext(), "An email has been sent !",
                            Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }else{
                    Toast.makeText(view.getContext(), "You have been logged out.",
                            Toast.LENGTH_SHORT).show();
                }

            }


        });

        Button buttonNo = popupView.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }


        });
    }

}
