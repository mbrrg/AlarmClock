package com.plushware.alarmclock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class AlarmService extends WakefulIntentService {
	public AlarmService() {
		super("AlarmService");
	}
	
	private static PendingIntent createTriggerIntent(Context context) {	
		Intent alarmIntent = new Intent(context, BroadcastDispatcher.class);
		alarmIntent.putExtra("Service", BroadcastDispatcher.SERVICE_ALARM);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
	
		return pi;
	}	
	
	public static void enableTrigger(Context context) {
		disableTrigger(context);
		
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		Settings s = new Settings(context);
	    Calendar calendar = Calendar.getInstance();
	   
        if (calendar.get(Calendar.HOUR_OF_DAY) < s.alarmHour ||
        	(calendar.get(Calendar.HOUR_OF_DAY) == s.alarmHour && calendar.get(Calendar.MINUTE) < s.alarmMinute)) {
            calendar.set(Calendar.HOUR_OF_DAY, s.alarmHour);
            calendar.set(Calendar.MINUTE, s.alarmMinute);
        } else {
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, s.alarmHour);
            calendar.set(Calendar.MINUTE, s.alarmMinute);
        }     		
        
        calendar.set(Calendar.SECOND, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, 
        		createTriggerIntent(context)); 
	}
	
	public static void disableTrigger(Context context) {
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = createTriggerIntent(context);
		manager.cancel(pendingIntent);
	}	
	
	@Override
	protected void doWakefulWork(Intent intent) {
		Log.d("AlarmService", "Sound the alarm!");
		
		Intent alarmIntent = new Intent(this, AlarmActivity.class);
		alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		startActivity(alarmIntent);
	}
}
