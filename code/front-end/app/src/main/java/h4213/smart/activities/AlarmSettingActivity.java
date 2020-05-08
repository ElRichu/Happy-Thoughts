package h4213.smart.activities;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import h4213.smart.services.AlarmReceiver;
import h4213.smart.R;

public class AlarmSettingActivity extends AppCompatActivity {

    TimePicker timepicker;
    Switch alarmOnOff;
    boolean isSet;
    int alarmHour;
    int alarmMinute;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        try {
            this.isSet = savedInstanceState.getBoolean("alarmSet", false);
            this.alarmHour = savedInstanceState.getInt("alarmHour", 0);
            this.alarmMinute = savedInstanceState.getInt("alarmMinute", 0);
        }catch(NullPointerException e){
            Toast.makeText(this, "First time coming here?", Toast.LENGTH_SHORT).show();
            this.isSet = false;
            this.alarmHour = 0;
            this.alarmMinute = 0;
        }

        this.timepicker = findViewById(R.id.timePicker1);
        this.alarmOnOff = findViewById(R.id.switchActivateAlarm);

        this.timepicker.setHour(alarmHour);
        this.timepicker.setMinute(alarmMinute);

        if(this.isSet){
            this.alarmOnOff.setChecked(true);
        }
        alarmOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> activateDeactivateAlarm(isChecked));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void activateDeactivateAlarm(boolean isChecked){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (isChecked) {
            Intent i = new Intent(this, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
                    PendingIntent.FLAG_ONE_SHOT);
            this.isSet=true;
            //java.util.Calendar
            //set the time with calendar to which you wich your alarm to launch
            Calendar calendar = Calendar.getInstance();
            Calendar now = Calendar.getInstance();

            this.alarmHour = this.timepicker.getHour();
            this.alarmMinute = this.timepicker.getMinute();
            /*if(now.getTimeInMillis()>calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);
            }*/
            calendar.set(Calendar.HOUR, alarmHour);
            calendar.set(Calendar.MINUTE, alarmMinute);
            try {
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),pi),pi);
            }catch(NullPointerException e){
                Toast.makeText(this, "Unhandled error - alarm didn't set", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "alarm set up in "+(calendar.get(Calendar.HOUR)*60+calendar.get(Calendar.MINUTE)-now.get(Calendar.HOUR)*60-now.get(Calendar.MINUTE)) +" min", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(this, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
                    PendingIntent.FLAG_ONE_SHOT);
            try {
                alarmManager.cancel(alarmManager.getNextAlarmClock().getShowIntent());
                Toast.makeText(this, "alarm shut down", Toast.LENGTH_SHORT).show();
            }catch(NullPointerException e){
                Toast.makeText(this, "Unhandled error - alarm didn't cancel", Toast.LENGTH_SHORT).show();
            }
            this.isSet = false;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("alarmHour",alarmHour);
        outState.putInt("alarmMinute",alarmMinute);
        if(isSet){
            outState.putBoolean("alarmSet",true);
        }else {
            outState.putBoolean("alarmSet", false);
        }
    }

    public void goToHome(View view){
        Intent intent = new Intent(this, HomepageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK) ;
        startActivity(intent);
    }
}
