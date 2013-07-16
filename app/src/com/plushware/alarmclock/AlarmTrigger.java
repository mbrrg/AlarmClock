package com.plushware.alarmclock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import static com.plushware.alarmclock.AlarmClock.DEBUG_MODE;

public class AlarmTrigger implements OnSharedPreferenceChangeListener {
	public static final String TAG = "AlarmTrigger";
	
	private static PendingIntent createTriggerIntent() {	
		/*Intent alarmIntent = new Intent(AlarmClock.INTENT_ALARM_ON);
		
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
	
		return pi;*/
		
		Context context = AlarmClock.getContext();
		
		Intent wakeupIntent = new Intent(context, WakeupReceiver.class);		
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, wakeupIntent, 0);		
		
		return pi;
	}	
	
	public static void enable() {
		disable();
		
		Context context = AlarmClock.getContext();
		
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		Settings s = new Settings(context);
	    Calendar calendar = Calendar.getInstance();
	   
	    if (!DEBUG_MODE) {
	        if (calendar.get(Calendar.HOUR_OF_DAY) < s.alarmHour ||
	        	(calendar.get(Calendar.HOUR_OF_DAY) == s.alarmHour && calendar.get(Calendar.MINUTE) < s.alarmMinute)) {
	            calendar.set(Calendar.HOUR_OF_DAY, s.alarmHour);
	            calendar.set(Calendar.MINUTE, s.alarmMinute);
	        } else {
	            calendar.add(Calendar.DATE, 1);
	            calendar.set(Calendar.HOUR_OF_DAY, s.alarmHour);
	            calendar.set(Calendar.MINUTE, s.alarmMinute);
	        }
	    } else {
	    	Log.d(TAG, "Debug mode enabled, setting alarm for next minute.");
	    	
	    	calendar.add(Calendar.MINUTE, 1);
	    }
                
        calendar.set(Calendar.SECOND, 0);
        
        Log.d(TAG, "Enabling repeating RTC wake up alarm every 24 hrs starting " + calendar.toString());

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, 
        		createTriggerIntent()); 
	}
	
	public static void disable() {
		Log.d(TAG, "Disabling wake up alarm.");
		
		Context context = AlarmClock.getContext();
		
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = createTriggerIntent();
		
		manager.cancel(pendingIntent);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key == Settings.KEY_ALARM_TIME || key == Settings.KEY_ALARM_ENABLED) {
			Settings settings = new Settings(sharedPreferences);
			
			if (settings.alarmEnabled) {
				enable();
			} else {
				disable();
			}		
		}	
	}	
}
