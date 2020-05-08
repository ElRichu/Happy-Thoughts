package h4213.smart.services;

import android.content.Intent;

import h4213.smart.activities.HomepageActivity;

public class AlarmService extends WakeIntentService {
    public AlarmService() {
        super("AlarmService");
    }

    /*call this alarme with :

    Intent i = new Intent(this, AlarmeManager.class);
    PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
                                                         PendingIntent.FLAG_ONE_SHOT);
    //java.util.Calendar
    //set the time with calendar to which you wich your alarm to launch
    Calendar calendar = Calendar.getInstance();
    //ex: alarm in 10 sec
    calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 10);
    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
     */

    @Override
    void doReminderWork(Intent intent) {
        //start reading tweets
        //go to the specified activity that reads tweets etc
        Intent i = new Intent(this, HomepageActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("readTweets",true);

        startActivity(i);
    }
}
