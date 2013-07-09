package com.plushware.alarmclock;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class AlarmTrigger {
	private static PendingIntent createIntent(Context context) {	
		Intent alarmIntent = new Intent(context, BroadcastDispatcher.class);
		alarmIntent.putExtra("Service", BroadcastDispatcher.SERVICE_ALARM);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
	
		return pi;
	}
	
	public static void enable(Context context) {
		disable(context);
		
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
        		createIntent(context)); 
	}
	
	public static void disable(Context context) {
		AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = createIntent(context);
		manager.cancel(pendingIntent);
	}
}
